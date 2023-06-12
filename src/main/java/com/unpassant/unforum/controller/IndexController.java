package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.PaginationDTO;
import com.unpassant.unforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "7") Integer size
                        ) {

        //查询论坛主页面内容
        PaginationDTO pagination = postService.list(page,size);
        model.addAttribute("pagination", pagination);

        return "index";
    }
}
