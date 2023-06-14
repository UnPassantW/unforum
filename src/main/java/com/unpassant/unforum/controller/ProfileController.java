package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.PaginationDTO;
import com.unpassant.unforum.model.User;
import com.unpassant.unforum.service.NotificationService;
import com.unpassant.unforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private PostService postService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action",value = "") String action,
                          Model model,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "7") Integer size){

        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        if ("posts".equals(action)) {
            PaginationDTO pagination = postService.list(user.getId(), page,size);
            model.addAttribute("section", "posts");
            model.addAttribute("sectionName", "我的帖子");
            model.addAttribute("pagination", pagination);
        } else if ("replies".equals(action)) {
            PaginationDTO pagination = notificationService.list(user.getId(),page,size);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pagination", pagination);
        }


        return "profile";
    }

}
