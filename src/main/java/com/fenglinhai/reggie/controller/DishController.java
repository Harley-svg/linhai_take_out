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
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CacheManager cacheManager;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        //清理所有菜品redis缓存数据
        //清理一部分缓存数据
        String key="dish_"+dishDto.getCategoryId()+"_1";
//        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(key);
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
        dishService.updateWithFlavor(dishDto);
        //清理所有菜品redis缓存数据
        String key="dish_"+dishDto.getCategoryId()+"_1";
//        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(key);
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

    /**
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {

        List<DishDto>dishDtoList=null;//需要返回的对象

        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();//dish_1212131313131_1 动态构造key
        //从redis中获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);

        //如果存在，直接返回，无需查询数据库
        if(dishDtoList!=null){
            return R.success(dishDtoList);
        }


        //构造查询条件
        dishDtoList=new ArrayList<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);

        List<Dish> list = dishService.list(queryWrapper);
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
        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }


}
