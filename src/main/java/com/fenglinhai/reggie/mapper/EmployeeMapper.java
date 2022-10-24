package com.fenglinhai.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglinhai.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Harley
 * @create 2022-10-14 20:59
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
