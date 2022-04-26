package com.hut.spring;

import lombok.Data;

/**
 * @Description: Bean的定义类
 * @Author: 众码纪
 * @date: 2022-04-26
 */
@Data
public class BeanDefinition {
    /** bean作用域 **/
    private ScopeEnum scope;
    /** bean类型 **/
    private Class beanType;
}
