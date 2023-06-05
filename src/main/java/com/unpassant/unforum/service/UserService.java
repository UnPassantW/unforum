package com.unpassant.unforum.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unpassant.unforum.mapper.UserMapper;
import com.unpassant.unforum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("account_id",user.getAccountId());
        User dbuser = userMapper.selectOne(qw);
        if (dbuser == null){
            //如果数据库里没有匹配用户则插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //否则更新
            dbuser.setGmtModified(System.currentTimeMillis());
            dbuser.setAvatarUrl(user.getAvatarUrl());
            dbuser.setName(user.getName());
            dbuser.setToken(user.getToken());
            userMapper.update(dbuser,null);
        }

    }
}
