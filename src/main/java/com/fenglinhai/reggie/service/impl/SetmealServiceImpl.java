package com.fenglinhai.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglinhai.reggie.common.CustomException;
import com.fenglinhai.reggie.dto.SetmealDto;
import com.fenglinhai.reggie.entity.Setmeal;
import com.fenglinhai.reggie.entity.SetmealDish;
import com.fenglinhai.reggie.mapper.SetmealMapper;
import com.fenglinhai.reggie.service.SetmealDishService;
import com.fenglinhai.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Harley
 * @create 2022-10-20 0:44
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息
        this.save(setmealDto);
        //保存菜单菜品到setmealDish表格
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish:setmealDishes){
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 删除套餐
     * @param list
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> list) {
        //select count(*) from setmeal where id in (1,2,3) and status=1
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,list).eq(Setmeal::getStatus,1);
        //如果不能删除，抛出一个业务异常
        int count=this.count(queryWrapper);//要删除的条数
        if(count>0){
            throw new CustomException("套餐在售卖，无法删除");
        }
        //如果可以删除，先删除套餐表中的数据
        this.removeByIds(list);

        //删除关系表中的数据
        //delete from setmeal_dish where setmeal_id in(1,2,3);
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,list);
        setmealDishService.remove(queryWrapper1);


    }

    /**
     * 根据id获取setmealDto
     * @param id
     * @return
     */
    @Override
    public SetmealDto getByWithDishes(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }
}
