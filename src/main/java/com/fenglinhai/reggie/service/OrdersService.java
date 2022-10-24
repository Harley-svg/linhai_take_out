package com.fenglinhai.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglinhai.reggie.entity.Orders;

/**
 * @author Harley
 * @create 2022-10-24 0:12
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
    void submit(Orders orders);
}
