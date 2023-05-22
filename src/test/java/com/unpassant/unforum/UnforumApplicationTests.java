package com.unpassant.unforum;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unpassant.unforum.mapper.PostMapper;
import com.unpassant.unforum.mapper.UserMapper;
import com.unpassant.unforum.model.Post;
import com.unpassant.unforum.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UnforumApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PostMapper postMapper;

    @Test
    void testsave(){
        User user01 = new User();
        user01.setAccountId("2");
        user01.setName("Test");
        userMapper.insert(user01);
    }

    @Test
    void testdelete(){
        userMapper.deleteById(2);
    }

    @Test
    void testupdate(){
        User user01 = new User();
        user01.setId(2);
        user01.setName("小丑");
        userMapper.updateById(user01);
    }

    @Test
    void testgetby(){
        QueryWrapper<User> qw = new QueryWrapper<User>();
        qw.eq("token","1");
        List<User> userList = userMapper.selectList(qw);
        System.out.println(userList);
    }

    @Test
    void testgetbyid(){
        User user = userMapper.selectById(7);
        System.out.println(user);
    }
    @Test
    void testselectlist(){
        QueryWrapper<com.unpassant.unforum.model.Post> Post = new QueryWrapper<>();
        List<Post> posts = postMapper.selectList(Post);
        System.out.println(posts);
    }


    @Test
    void testgetbypage(){
        IPage page = new Page(1,1);
        userMapper.selectPage(page,null);
        System.out.println("当前页码值为：" + page.getCurrent());
        System.out.println("每页显示数为：" + page.getSize());
        System.out.println("总共页数为：" + page.getPages());
        System.out.println("一共多少条数据：" + page.getTotal());
        System.out.println("数据：" + page.getRecords());
    }
}
