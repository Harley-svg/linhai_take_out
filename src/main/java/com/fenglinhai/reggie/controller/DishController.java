package com.fenglinhai.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglinhai.reggie.common.R;
import com.fenglinhai.reggie.dto.DishDto;
import com.fenglinhai.reggie.entity.Category;
import com.fenglinhai.reggie.entity.Dish;
import com.fenglinhai.reggie.entity.DishFlavor;
import com.fenglinhai.reggie.entity.Employee;
import com.fenglinhai.reggie.service.CategoryService;
import com.fenglinhai.reggie.service.DishFlavorService;
import com.fenglinhai.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley
 * @create 2022-10-20 20:26
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString() + "dishdto1111");
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page pageInfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(name != null, Dish::getName, name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            Dish dish = records.get(i);
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            dishDto.setCategoryName(category.getName());
            list.add(dishDto);

        }


        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);


    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString() + "dishdto1111");
        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    //    @GetMapping("/list")
//    public R<List<Dish>>list(Dish dish){
//        log.info("dish为"+dish+dish.getCategoryId()+"111");
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//
//        queryWrapper.eq(Dish::getStatus,1);
//
//        queryWrapper.orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        log.info(dish.toString());

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());

        queryWrapper.eq(Dish::getStatus, 1);



        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto>dishDtoList=new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Dish dishThis = list.get(i);
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishThis,dishDto);

            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishThis.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);

            dishDto.setFlavors(dishFlavors);
            dishDtoList.add(dishDto);

        }
        return R.success(dishDtoList);
    }


}
