package com.hut.service;

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
public class UserService {

    @Autowired
    public PowerService powerService;

    public void login() {
        System.out.println("登陆了啊！！！！");
    }


}
