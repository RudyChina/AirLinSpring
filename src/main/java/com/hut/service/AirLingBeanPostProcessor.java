package com.hut.service;

import com.hut.spring.BeanPostProcessor;
import com.hut.spring.annotation.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description bean的后置处理器
 * @author 众码纪
 * @date 2022/4/29 15:18
 */
@Component
public class AirLingBeanPostProcessor implements BeanPostProcessor {

    /**
     * 初始化前处理
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    @Override
    public Object postProcessorBeforeInitialization(Object bean, String beanName) throws Exception {
        //jdk动态代理
        Object proxyBean = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
            System.out.println("before init 前置切面 invoked");
            //目标方法执行
            method.invoke(bean,args);
            System.out.println("before init 后置切面 invoked");
            return proxy;
        });
        return proxyBean;
    }

    /**
     * 初始化后处理
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    @Override
    public Object postProcessorAfterInitialization(Object bean, String beanName) throws Exception {
        //jdk动态代理
        Object proxyBean = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
            System.out.println("after init 前置切面 invoked");
            //目标方法执行
            method.invoke(bean,args);
            System.out.println("after init 后置切面 invoked");
            return proxy;
        });
        return proxyBean;
    }
}
