package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.PostDTO;
import com.unpassant.unforum.model.Post;
import com.unpassant.unforum.model.User;
import com.unpassant.unforum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private PostService postService;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model) {

        //内容回显
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        //非空条件判断
        if(title == null || title.equals("")){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if(tag == null || tag.equals("")){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        //获取发布内容用户信息
        User user = (User)request.getSession().getAttribute("user");

        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }

        Post post = new Post();
        post.setTitle(title);
        post.setDescription(description);
        post.setCreator(user.getId());
        post.setTags(tag);
        if (id != null) {
            post.setId(id);
        }

        postService.createOrUpdate(post);

        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){

        PostDTO post = postService.selectById(id);
        model.addAttribute("title",post.getTitle());
        model.addAttribute("description",post.getDescription());
        model.addAttribute("tag",post.getTags());
        model.addAttribute("id",post.getId());

        return "publish";
    }
}
