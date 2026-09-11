package com.itheima.aspect.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//自定义注解
@Target(ElementType.METHOD) //声明当前这个注解可以标注的位置 METHOD:代表此注解可以标注在方法上
@Retention(RetentionPolicy.RUNTIME) //声明当前这个注解在哪个阶段生效 RUNTIME:代表此注解在运行时阶段还保留
public @interface LogOperation {
}