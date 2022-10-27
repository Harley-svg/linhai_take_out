package com.fenglinhai.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglinhai.reggie.common.R;
import com.fenglinhai.reggie.dto.DishDto;
import com.fenglinhai.reggie.dto.SetmealDto;
import com.fenglinhai.reggie.entity.Category;
import com.fenglinhai.reggie.entity.Dish;
import com.fenglinhai.reggie.entity.Setmeal;
import com.fenglinhai.reggie.entity.SetmealDish;
import com.fenglinhai.reggie.service.CategoryService;
import com.fenglinhai.reggie.service.SetmealDishService;
import com.fenglinhai.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harley
 * @create 2022-10-21 15:01
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取单个setmealDto
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto>get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByWithDishes(id);
        return R.success(setmealDto);
    }

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @CacheEvict(value = "setmealCache",allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);

        return R.success("保存套餐信息成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page pageInfo = new Page(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(name != null, Setmeal::getName, name);

        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            Setmeal setmeal = records.get(i);
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            Category category = categoryService.getById(setmeal.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            list.add(setmealDto);

        }


        setmealDtoPage.setRecords(list);

        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @CacheEvict(value = "setmealCache",allEntries = true)//allEntries = true-->删除setmealCache下的所有缓存数据
    @DeleteMapping
    public R<String>delete(@RequestParam List<Long>ids){//此时要与前端的传参ids一致，如果此时为id,就无法正确带入参数
        log.info(ids.toString());
        setmealService.removeWithDish(ids);
        return R.success("套餐已经被成功删除");
    }

    @PostMapping("/status/{id}")
    public R<String>update(@RequestParam Long ids,@PathVariable int id){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId,ids);
        Setmeal setmeal = setmealService.getOne(queryWrapper);
        setmeal.setStatus(id);
        setmealService.updateById(setmeal);
        return R.success("套餐状态1修改成功");

    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+#setmeal.status")
    public R<List<SetmealDto>>list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //获取套餐列表
        List<Setmeal> setmealList = setmealService.list(queryWrapper);

        List<SetmealDto>setmealDtoList=new ArrayList<>();

        for (int i=0;i<setmealList.size();i++){
            Setmeal setmealSingle = setmealList.get(i);
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmealSingle,setmealDto);
            LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealSingle.getId());
            //每个套餐里面的菜品
            List<SetmealDish> setmealDishList = setmealDishService.list(lambdaQueryWrapper);
            setmealDto.setSetmealDishes(setmealDishList);

            setmealDtoList.add(setmealDto);

        }
        return R.success(setmealDtoList);
    }
}
