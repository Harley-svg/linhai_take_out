package com.fenglinhai.reggie.dto;

import com.fenglinhai.reggie.entity.Dish;
import com.fenglinhai.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
