package com.fenglinhai.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglinhai.reggie.entity.User;
import com.fenglinhai.reggie.mapper.UserMapper;
import com.fenglinhai.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Harley
 * @create 2022-10-22 0:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
}
