package com.fenglinhai.reggie.dto;

import com.fenglinhai.reggie.entity.Setmeal;
import com.fenglinhai.reggie.entity.SetmealDish;

import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
