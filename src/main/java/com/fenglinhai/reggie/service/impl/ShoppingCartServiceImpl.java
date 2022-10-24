package com.fenglinhai.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglinhai.reggie.entity.ShoppingCart;
import com.fenglinhai.reggie.mapper.ShoppingCartMapper;
import com.fenglinhai.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author Harley
 * @create 2022-10-23 0:54
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>implements ShoppingCartService {
}
