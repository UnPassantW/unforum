package com.unpassant.unforum.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unpassant.unforum.dto.CommentDTO;
import com.unpassant.unforum.enums.CommentTypeEnum;
import com.unpassant.unforum.enums.NotificationStatusEnum;
import com.unpassant.unforum.enums.NotificationTypeEnum;
import com.unpassant.unforum.exception.CustomErrorCode;
import com.unpassant.unforum.exception.CustomException;
import com.unpassant.unforum.mapper.CommentMapper;
import com.unpassant.unforum.mapper.NotificationMapper;
import com.unpassant.unforum.mapper.PostMapper;
import com.unpassant.unforum.mapper.UserMapper;
import com.unpassant.unforum.model.Comment;
import com.unpassant.unforum.model.Notification;
import com.unpassant.unforum.model.Post;
import com.unpassant.unforum.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        //检查父类ID是否存在或为0
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomException(CustomErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //检查父类类型是否存在
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomException(CustomErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbcomment = commentMapper.selectById(comment.getParentId());
            if (dbcomment == null){
                throw new CustomException(CustomErrorCode.COMMENT_NOT_FOUND);
            }

            //为产生通知获取outerTitle帖子标题
            Post dbpost = postMapper.selectById(dbcomment.getParentId());
            if(dbpost == null){
                throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
            }

            commentMapper.insert(comment);
            //增加父评论的被评论数
            dbcomment.setCommentCount(dbcomment.getCommentCount() + 1);
            commentMapper.updateById(dbcomment);


            //产生通知
            createNotify(comment, dbcomment.getCommentator(), commentator.getName(), dbpost.getTitle(), NotificationTypeEnum.REPLY_COMMENT, dbpost.getId());
        } else {
            //回复问题
            Post dbpost = postMapper.selectById(comment.getParentId());
            if(dbpost == null){
                throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加帖子的被评论数
            dbpost.setCommentCount(dbpost.getCommentCount() + 1);
            postMapper.updateById(dbpost);

            //产生通知
            createNotify(comment, dbpost.getCreator(), commentator.getName(),dbpost.getTitle(),NotificationTypeEnum.REPLY_POST, dbpost.getId());
        }
    }

    private void createNotify(Comment comment, Integer receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Integer outerId) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        QueryWrapper<Comment> commentqw = new QueryWrapper<>();
        commentqw.eq("parent_id",id);
        commentqw.eq("type", type.getType());
        commentqw.orderByDesc("gmt_create");
        List<Comment> comments = commentMapper.selectList(commentqw);

        if (comments.size() == 0){
            return new ArrayList<>();
        }
        //获取去重后的评论者
        List<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).distinct().collect(Collectors.toList());

        //获取评论者并转化为User map
        QueryWrapper<User> userqw = new QueryWrapper<>();
        userqw.in("id",commentators);
        List<User> users = userMapper.selectList(userqw);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comments为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
