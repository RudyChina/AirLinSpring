package com.hut.spring;

public interface BeanPostProcessor {

    Object postProcessorBeforeInitialization(Object bean,String beanName) throws Exception;

    Object postProcessorAfterInitialization(Object bean,String beanName) throws Exception;
}
