package com.fenglinhai.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglinhai.reggie.entity.Employee;
import com.fenglinhai.reggie.mapper.EmployeeMapper;
import com.fenglinhai.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author Harley
 * @create 2022-10-14 21:01
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
