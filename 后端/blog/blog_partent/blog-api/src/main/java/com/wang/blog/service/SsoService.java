package com.wang.blog.service;

import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.LoginParam;
import com.wang.blog.vo.param.SsoParams;

/**
 * @author 王家俊
 */
public interface SsoService {
    /**
     * 注册
     */
    Result register(SsoParams ssoParams);
}
