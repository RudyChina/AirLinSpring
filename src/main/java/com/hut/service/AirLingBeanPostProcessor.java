package com.hut.service;

import cn.hutool.core.util.StrUtil;
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
        return bean;
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
        if (StrUtil.equals("userService", beanName)) {
            System.out.println(beanName);
            //jdk动态代理
            Object proxyBean = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println(method.getName()+" ==after init 切面逻辑");
                    //目标方法执行
                    return method.invoke(bean, args);
                }
            });
            System.out.println("------------after init method invoke----------");
            return proxyBean;
        }
        //System.out.println(beanName);

        return bean;
    }
}
