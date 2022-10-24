package com.fenglinhai.reggie.common;

import com.fenglinhai.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 * @author Harley
 * @create 2022-10-17 17:25
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})//加个这两个注解的类出现的异常都会被本类处理
@ResponseBody
@Slf4j
public class GlobalException {
    /**
     * 进行异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String msg=s[2];
            return R.error(msg+"已存在");
        }
        return R.error("未知错误");
    }

    /**
     * 自定义异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
