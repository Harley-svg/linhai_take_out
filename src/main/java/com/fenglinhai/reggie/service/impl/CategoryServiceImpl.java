package com.fenglinhai.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglinhai.reggie.common.CustomException;
import com.fenglinhai.reggie.entity.Category;
import com.fenglinhai.reggie.entity.Dish;
import com.fenglinhai.reggie.entity.Setmeal;
import com.fenglinhai.reggie.mapper.CategoryMapper;
import com.fenglinhai.reggie.service.CategoryService;
import com.fenglinhai.reggie.service.DishService;
import com.fenglinhai.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Harley
 * @create 2022-10-19 21:31
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //根据id查询dish
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count>0){
            //已经关联菜品，抛出异常
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }
        //查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1>0){
            //已经关联套餐，抛出异常
            throw new CustomException("当前分类下关联了套餐，无法删除");
        }

        //正常删除分类
        super.removeById(id);

    }
}
