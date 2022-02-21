package com.wang.blog.service;

import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.LoginParam;

/**
 * @author 王家俊
 */
public interface LoginService {
    /**
     * 登陆接口
     */
    Result login(LoginParam loginParam);

    /**
     * 验证token
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 登出
     * @return
     */
    Result logout(String token);
}
