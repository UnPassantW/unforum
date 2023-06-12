package com.unpassant.unforum.service;

import com.unpassant.unforum.enums.CommentTypeEnum;
import com.unpassant.unforum.exception.CustomErrorCode;
import com.unpassant.unforum.exception.CustomException;
import com.unpassant.unforum.mapper.CommentMapper;
import com.unpassant.unforum.mapper.PostMapper;
import com.unpassant.unforum.model.Comment;
import com.unpassant.unforum.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PostMapper postMapper;

    @Transactional
    public void insert(Comment comment) {
        //检查父类ID是否存在或为0
        if (comment.getParent_id() == null || comment.getParent_id() == 0) {
            throw new CustomException(CustomErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        //检查父类类型是否存在
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomException(CustomErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbcomment = commentMapper.selectById(comment.getParent_id());
            if (dbcomment == null){
                throw new CustomException(CustomErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else {
            //回复问题
            Post dbpost = postMapper.selectById(comment.getParent_id());
            if(dbpost == null){
                throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
            }
            commentMapper.insert(comment);
            dbpost.setCommentCount(dbpost.getCommentCount() + 1);
            postMapper.updateById(dbpost);
        }
    }
}
