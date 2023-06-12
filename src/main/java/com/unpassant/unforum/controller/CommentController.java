package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.CommentDTO;
import com.unpassant.unforum.dto.ResultDTO;
import com.unpassant.unforum.exception.CustomErrorCode;
import com.unpassant.unforum.mapper.CommentMapper;
import com.unpassant.unforum.model.Comment;
import com.unpassant.unforum.model.User;
import com.unpassant.unforum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return ResultDTO.errorOf(CustomErrorCode.NO_LOGIN);
        }

        Comment comment = new Comment();
        comment.setParent_id(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmt_create(System.currentTimeMillis());
        comment.setGmt_modified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLike_count(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}
