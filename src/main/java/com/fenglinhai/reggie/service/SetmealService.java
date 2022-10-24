package com.fenglinhai.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglinhai.reggie.dto.SetmealDto;
import com.fenglinhai.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author Harley
 * @create 2022-10-20 0:43
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 保存套餐名称
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐
     * @param list
     */
    void removeWithDish(List<Long> list);
}
