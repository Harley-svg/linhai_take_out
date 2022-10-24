package com.fenglinhai.reggie.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 * @author Harley
 * @create 2022-10-19 15:50
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
