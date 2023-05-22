package com.unpassant.unforum.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unpassant.unforum.dto.PostDTO;
import com.unpassant.unforum.mapper.PostMapper;
import com.unpassant.unforum.mapper.UserMapper;
import com.unpassant.unforum.model.Post;
import com.unpassant.unforum.model.User;
import com.unpassant.unforum.service.PostService;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostService postService;


    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length !=0)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();

                    //mybatis 按条件查询
                    QueryWrapper<User> qw = new QueryWrapper<User>();
                    qw.eq("token",token);
                    User user = userMapper.selectOne(qw);

                    //测试页面打算存入session的cookie用户
                    //System.out.println(user);

                    if (user != null){

                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }


        //查询论坛主页面内容
        List<PostDTO> postList = postService.list();
        model.addAttribute("posts",postList);

        return "index";
    }
}
