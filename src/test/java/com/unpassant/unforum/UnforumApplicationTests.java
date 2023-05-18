package com.unpassant.unforum;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unpassant.unforum.dao.UserDao;
import com.unpassant.unforum.dto.AccessTokenDTO;
import com.unpassant.unforum.dto.GithubUser;
import com.unpassant.unforum.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class UnforumApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void testsave(){
        User user01 = new User();
        user01.setAccount_id("2");
        user01.setName("Test");
        userDao.insert(user01);
    }

    @Test
    void testdelete(){
        userDao.deleteById(2);
    }

    @Test
    void testupdate(){
        User user01 = new User();
        user01.setId(2);
        user01.setName("小丑");
        userDao.updateById(user01);
    }

    @Test
    void testgetbyid(){
        User user01 = userDao.selectById(1);
        System.out.println(user01);
    }

    @Test
    void testgetbypage(){
        IPage page = new Page(1,1);
        userDao.selectPage(page,null);
        System.out.println("当前页码值为：" + page.getCurrent());
        System.out.println("每页显示数为：" + page.getSize());
        System.out.println("总共页数为：" + page.getPages());
        System.out.println("一共多少条数据：" + page.getTotal());
        System.out.println("数据：" + page.getRecords());
    }
}
