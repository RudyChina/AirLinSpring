package com.hut.service;

import com.hut.spring.BeanNameAware;
import com.hut.spring.InitializingBean;
import com.hut.spring.annotation.Autowired;
import com.hut.spring.annotation.Component;
import com.hut.spring.annotation.Scope;

/**
 * @Description: 测试Bean对象
 * @Author: 众码纪
 * @date: 2022-04-26
 */
@Component
@Scope
public class UserService implements UserServiceInterface, InitializingBean, BeanNameAware {

    @Autowired
    private PowerService powerService;

    @Override
    public void login() {
        System.out.println("登陆了啊！！！！");
    }
    @Override
    public void userLogin(){
      powerService.checkPower();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("属性赋值完毕了，初始化了，afterPropertiesSetting.........................");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("beanNameAware回调已执行..........入参，name:"+name);
    }


}
