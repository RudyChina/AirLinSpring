package com.hut.spring.annotation;

import com.hut.spring.ScopeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: Bean作用域注解
 * @Author: 众码纪
 * @date: 2022-04-26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scope {

    ScopeEnum value() default ScopeEnum.singleTon;
}
