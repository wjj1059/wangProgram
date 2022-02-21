package com.wang.blog.controller;

import com.wang.blog.service.LoginService;
import com.wang.blog.service.SsoService;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.LoginParam;
import com.wang.blog.vo.param.SsoParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王家俊
 */
@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private SsoService ssoService;

    @PostMapping
    public Result register(@RequestBody SsoParams ssoParams){
        //sso单点登陆
        return ssoService.register(ssoParams);
    }
}
