package com.hut.spring;

public interface InitializingBean {

    /**
     * 所有属性赋值完成后执行的方法
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception;
}
