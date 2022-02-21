package com.wang.blog.controller;

import com.wang.blog.service.LoginService;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王家俊
 */
@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        //登陆 验证用户 访问用户
        return loginService.login(loginParam);
    }

}
