package com.fenglinhai.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglinhai.reggie.dto.DishDto;
import com.fenglinhai.reggie.entity.Dish;

/**
 * @author Harley
 * @create 2022-10-20 0:42
 */
public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish和dish_flavor
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    DishDto getByWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    void updateWithFlavor(DishDto dishDto);
}
