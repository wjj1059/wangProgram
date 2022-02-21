package com.wang.blog.controller;

import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.utils.UserThreadLocal;
import com.wang.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王家俊
 */
@RestController
@RequestMapping("test")
public class TestController {
    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
