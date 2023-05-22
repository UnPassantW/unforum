package com.unpassant.unforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unpassant.unforum.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
