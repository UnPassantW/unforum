package com.unpassant.unforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unpassant.unforum.model.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
