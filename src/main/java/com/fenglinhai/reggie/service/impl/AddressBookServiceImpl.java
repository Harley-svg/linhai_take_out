package com.fenglinhai.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglinhai.reggie.entity.AddressBook;
import com.fenglinhai.reggie.mapper.AddressBookMapper;
import com.fenglinhai.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author Harley
 * @create 2022-10-22 17:19
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>implements AddressBookService {
}
