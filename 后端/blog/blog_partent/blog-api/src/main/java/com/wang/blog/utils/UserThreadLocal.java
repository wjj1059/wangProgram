package com.wang.blog.utils;

import com.wang.blog.dao.pojo.SysUser;

/**
 * @author 王家俊
 */
public class UserThreadLocal {
    private UserThreadLocal(){}
    //线程变量隔离(线程安全解决方案之一)
    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }

}
