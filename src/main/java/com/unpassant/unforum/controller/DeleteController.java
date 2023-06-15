package com.unpassant.unforum.controller;

import com.unpassant.unforum.cache.TagCache;
import com.unpassant.unforum.dto.PostDTO;
import com.unpassant.unforum.mapper.PostMapper;
import com.unpassant.unforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DeleteController {

    @Autowired
    private PostService postService;

    @GetMapping("/delete/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){

        Integer result = postService.deleteById(id);

        model.addAttribute("deleteResult",result);

        return "redirect:/profile/posts";
    }
}
