package com.unpassant.unforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unpassant.unforum.model.Post;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface PostMapper extends BaseMapper<Post> {

}
