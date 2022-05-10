package com.test;

import com.hut.service.UserServiceInterface;
import com.hut.spring.AirLinApplicationContext;
import com.hut.spring.AppConfig;

/**
 * @Description: 测试spring启动类
 * @Author: 众码纪
 * @date: 2022-04-26
 */
public class SpringTest {
    /**
     * 测试启动类
     * @param args
     */
    public static void main(String[] args) {
        AirLinApplicationContext applicationContext = new AirLinApplicationContext(AppConfig.class);

        UserServiceInterface userService = (UserServiceInterface) applicationContext.getBean("userService");
        /*userService.login();*/
        userService.userLogin();
/*
        System.out.println(userService);*/


    }
}
