package com.itheima.handler;

import com.itheima.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class) //处理DuplicateKeyException类型的异常
    public Result handlerException(DuplicateKeyException e){//这个参数用于接收捕获到的异常
        if (e.getMessage().contains("dept.name")){
            log.error("部门名称已存在");
            return Result.error("部门名称已存在");
        }
        return Result.error("对不起,操作失败,请联系管理员");
    }

    @ExceptionHandler(Exception.class) //处理Exception类型的异常
    public Result handlerException(Exception e){//这个参数用于接收捕获到的异常
        log.error("服务器发生异常", e);
        //捕获到异常之后，响应一个标准的Result
        return Result.error("对不起,操作失败,请联系管理员");
    }
}