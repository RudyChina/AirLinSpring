package com.hut.service;

import com.hut.spring.annotation.Component;

/**
 * @Description: 测试Bean对象
 * @Author: 众码纪
 * @date: 2022-04-26
 */
@Component
public class UserService {

    public void login() {
        System.out.println("登陆了啊！！！！");
    }
}
