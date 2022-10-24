package com.fenglinhai.reggie.common;

/**
 * 自定义业务异常类
 * @author Harley
 * @create 2022-10-20 0:57
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
