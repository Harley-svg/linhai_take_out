package com.fenglinhai.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglinhai.reggie.entity.Category;

/**
 * @author Harley
 * @create 2022-10-19 21:30
 */
public interface CategoryService extends IService<Category> {
     void remove(Long id);
}
