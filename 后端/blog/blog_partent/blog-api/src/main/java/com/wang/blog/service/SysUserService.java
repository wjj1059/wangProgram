package com.wang.blog.service;

import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.UserVo;


/**
 * @author 王家俊
 */

public interface SysUserService {
    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    SysUser findUserById(Long id);

    /**
     * 登陆验证账号密码
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 根据token返回用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存
     * @param sysUser
     */
    void save(SysUser sysUser);

    UserVo findUserVoById(Long authorId);
}
