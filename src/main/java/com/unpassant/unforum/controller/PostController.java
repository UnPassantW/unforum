package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.CommentDTO;
import com.unpassant.unforum.dto.PostDTO;
import com.unpassant.unforum.enums.CommentTypeEnum;
import com.unpassant.unforum.service.CommentService;
import com.unpassant.unforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{id}")
    public String post(@PathVariable(name = "id") Integer id, Model model){
        PostDTO postDTO = postService.selectById(id);

        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        //增加阅读数
        postService.incView(id);

        model.addAttribute("post",postDTO);
        model.addAttribute("comments",comments);
        return "post";
    }

}
