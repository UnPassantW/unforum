package com.unpassant.unforum.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unpassant.unforum.dao.UserDao;
import com.unpassant.unforum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserDao userDao;


    @GetMapping("/")
    public String index(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                String token = cookie.getValue();

                //mybatis 按条件查询
                QueryWrapper<User> qw = new QueryWrapper<User>();
                qw.eq("token",token);
                User user = userDao.selectOne(qw);
                System.out.println(user);
                if (user != null){

                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        return "index";
    }
}
