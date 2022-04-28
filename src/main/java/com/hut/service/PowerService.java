package com.hut.service;

import com.hut.spring.annotation.Component;

/**
 * @description 权限service
 * @author 众码纪
 * @date 2022/4/28 16:52
 */
@Component
public class PowerService {

    public void checkPower(){
        System.out.println(this+"检查权限中。。。。");
    }

}
