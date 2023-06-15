package com.unpassant.unforum.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unpassant.unforum.dto.PaginationDTO;
import com.unpassant.unforum.dto.PostDTO;
import com.unpassant.unforum.exception.CustomErrorCode;
import com.unpassant.unforum.exception.CustomException;
import com.unpassant.unforum.mapper.PostMapper;
import com.unpassant.unforum.mapper.UserMapper;
import com.unpassant.unforum.model.Post;
import com.unpassant.unforum.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Integer totalPage;
        //分页总数
        Integer totalCount = postMapper.selectCount(null);

        //计算totalPage
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        //页码越界处理
        if (page > totalPage) {
            page = totalPage;
        }
        if (page < 1) {
            page = 1;
        }

        paginationDTO.setPagination(totalPage,page);

        //定义分页
        IPage ipage = new Page(page, size);
        //倒序排列
        QueryWrapper<Post> qw = new QueryWrapper<>();
        qw.orderByDesc("gmt_create");
        //分页查询
        postMapper.selectPage(ipage,qw);
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
        paginationDTO.setData(postDTOList);

        return paginationDTO;
    }

    //查找某用户所发的帖子
    public PaginationDTO list(int userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        //定义查询条件
        QueryWrapper<Post> qw = new QueryWrapper<>();
        qw.eq("creator",userId);

        Integer totalPage;
        //分页总数
        Integer totalCount = postMapper.selectCount(qw);

        //计算totalPage
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        //页码越界处理
        if (page > totalPage) {
            page = totalPage;
        }
        if (page < 1) {
            page = 1;
        }

        paginationDTO.setPagination(totalPage,page);

        //越界判断后查询
        //定义分页
        IPage ipage = new Page(page, size);

        //分页查询
        postMapper.selectPage(ipage,qw);
        List<Post> posts = ipage.getRecords();

        //通过PostDTO关联User以便获取Avatar头像地址
        List<PostDTO> postDTOList = new ArrayList<>();
        for (Post post : posts) {
            User user = userMapper.selectById(post.getCreator());
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post,postDTO);
            postDTO.setUser(user);
            postDTOList.add(postDTO);
        }
        paginationDTO.setData(postDTOList);

        return paginationDTO;
    }

    public PostDTO selectById(Integer id) {
        Post post = postMapper.selectById(id);
        //异常处理
        if(post == null){
            throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
        }
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post,postDTO);
        User user = userMapper.selectById(post.getCreator());
        postDTO.setUser(user);
        return postDTO;
    }

    public void createOrUpdate(Post post) {
        if ( null == post.getId() || post.getId() == 0){
            //创建帖子
            post.setGmtCreate(System.currentTimeMillis());
            post.setGmtModified(post.getGmtCreate());
            post.setViewCount(0);
            post.setLikeCount(0);
            post.setCommentCount(0);
            post.setVersion(1);
            postMapper.insert(post);
        }else{
            //更新帖子
            post.setGmtModified(System.currentTimeMillis());
            int updated = postMapper.updateById(post);
            if (updated != 1) {
                throw new CustomException(CustomErrorCode.POST_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        Post updatePost = postMapper.selectById(id);
        updatePost.setViewCount(updatePost.getViewCount() + 1);

        postMapper.updateById(updatePost);
    }

    private String dealTags(String tags){
        // 处理 tags 。按照逗号分割。
        String[] tagsArr = StringUtils.split(tags, ",");
        String tempTags = StringUtils.join(tagsArr, "|");
        String format = "tags REGEXP '(%s)'";
        String result = String.format(format, tempTags);
        return  result;
    }
    public List<PostDTO> selectRelated(PostDTO postDTO) {
        if (StringUtils.isBlank(postDTO.getTags())){
            return new ArrayList<>();
        }
        QueryWrapper<Post> qw = new QueryWrapper<>();
        qw.orderByDesc("gmt_create");//时间倒序
        qw.ne("id",postDTO.getId());//排除自己
        qw.apply(dealTags(postDTO.getTags()));//拼接正则表达式

        List<Post> posts = postMapper.selectList(qw);
        List<PostDTO> postDTOS = posts.stream().map(p -> {
            PostDTO tempPostDTO1 = new PostDTO();
            BeanUtils.copyProperties(p,tempPostDTO1);
            return tempPostDTO1;
        }).collect(Collectors.toList());
        return postDTOS;
    }

    public Integer deleteById(Integer id) {
        int result = postMapper.deleteById(id);
        //result为delete受影响行数
        return  result;
    }
}
