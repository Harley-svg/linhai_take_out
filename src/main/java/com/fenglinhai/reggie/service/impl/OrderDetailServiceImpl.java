package com.fenglinhai.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglinhai.reggie.entity.OrderDetail;
import com.fenglinhai.reggie.mapper.OrderDetailMapper;
import com.fenglinhai.reggie.service.OrderDetailService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

/**
 * @author Harley
 * @create 2022-10-24 0:15
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>implements OrderDetailService {
}
