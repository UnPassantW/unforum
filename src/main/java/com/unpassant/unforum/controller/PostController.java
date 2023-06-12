package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.PostDTO;
import com.unpassant.unforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/post/{id}")
    public String post(@PathVariable(name = "id") Integer id,
                       Model model){
        PostDTO postDTO = postService.selectById(id);
        //增加阅读树
        postService.incView(id);
        model.addAttribute("post",postDTO);
        return "post";
    }

}
