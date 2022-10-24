package com.fenglinhai.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglinhai.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Harley
 * @create 2022-10-22 0:39
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
