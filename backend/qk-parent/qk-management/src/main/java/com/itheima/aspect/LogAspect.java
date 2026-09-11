package com.itheima.aspect;

import com.itheima.entity.OperateLog;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.util.UserHoler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.itheima.aspect.anno.LogOperation)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString( args);

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;
            OperateLog log = new OperateLog();
            log.setOperateUserId(UserHoler.getCurrentUser());
            log.setOperateTime(LocalDateTime.now());
            log.setClassName(joinPoint.getTarget().getClass().getName());
            log.setMethodName(joinPoint.getSignature().getName());
            log.setMethodParams(methodParams);
            log.setReturnValue(e.getMessage());
            log.setCostTime(costTime);
            operateLogMapper.insert(log);
            throw e;
        }

        long costTime = System.currentTimeMillis() - startTime;

        OperateLog log = new OperateLog();
        log.setOperateUserId(UserHoler.getCurrentUser());
        log.setOperateTime(LocalDateTime.now());
        log.setClassName(joinPoint.getTarget().getClass().getName());
        log.setMethodName(joinPoint.getSignature().getName());
        log.setMethodParams(methodParams);
        log.setReturnValue(result != null ? result.toString() : null);
        log.setCostTime(costTime);
            
        operateLogMapper.insert(log);
        return result;
    }
}