package com.unpassant.unforum.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unpassant.unforum.mapper.UserMapper;
import com.unpassant.unforum.model.User;
import com.unpassant.unforum.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class  SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();

                    //mybatis 按条件查询
                    QueryWrapper<User> qw = new QueryWrapper<>();
                    qw.eq("token", token);
                    User user = userMapper.selectOne(qw);

                    //测试页面打算存入session的cookie用户
                    //System.out.println(user);

                    if (user != null) {
                        request.getSession().setAttribute("user", user);

                        //获取未读消息数量
                        Integer unreadCount = notificationService.unreadCount(user.getId());
                        request.getSession().setAttribute("unreadCount", unreadCount);

                    }
                    break;
                }
            }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
