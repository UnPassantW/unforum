package com.unpassant.unforum.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.unpassant.unforum.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
