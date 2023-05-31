package com.unpassant.unforum.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unpassant.unforum.dto.PaginationDTO;
import com.unpassant.unforum.dto.PostDTO;
import com.unpassant.unforum.mapper.PostMapper;
import com.unpassant.unforum.mapper.UserMapper;
import com.unpassant.unforum.model.Post;
import com.unpassant.unforum.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size){

        /*（不用mybatis_plus才需要改offset替换分页查询的属性）
        分页的页面尺寸公式转化 size*(page-1)
        Integer offset = size * (page - 1);
        */

        PaginationDTO paginationDTO = new PaginationDTO();



        //分页总数
        Integer totalCount = postMapper.selectCount(null);
        paginationDTO.setPagination(totalCount,page,size);

        //页码越界处理
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }

        //越界判断后查询
        //定义分页
        IPage ipage = new Page(page, size);
        //分页查询
        postMapper.selectPage(ipage,null);
        List<Post> posts = ipage.getRecords();

        /* OLD 简单查询Post数据
        QueryWrapper<Post> Post = new QueryWrapper<>();
        List<Post> posts = postMapper.selectList(Post);
        */

        /*调试
        //测试分页输出
        System.out.println("当前页码值为：" + ipage.getCurrent());
        System.out.println("每页显示数为：" + ipage.getSize());
        System.out.println("总共页数为：" + ipage.getPages());
        System.out.println("一共多少条数据：" + ipage.getTotal());
        System.out.println("数据：" + ipage.getRecords());*/

        //通过PostDTO关联User以便获取Avatar头像地址
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post : posts) {
            User user = userMapper.selectById(post.getCreator());
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post,postDTO);
            postDTO.setUser(user);
            postDTOList.add(postDTO);
        }
        paginationDTO.setPosts(postDTOList);

        return paginationDTO;
    }
}
