package com.wang.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.service.SsoService;
import com.wang.blog.service.SysUserService;
import com.wang.blog.utils.JwtUtils;
import com.wang.blog.vo.ErrorCode;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.LoginParam;
import com.wang.blog.vo.param.SsoParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author 王家俊
 */
@Service
@Transactional
public class SsoServiceImpl implements SsoService {

    //加密盐
    private static final String slat = "wang!@#";
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 注册
     * @param ssoParams
     * @return
     */
    @Override
    public Result register(SsoParams ssoParams) {
        /**
         * 1：判断参数是否合法
         * 2:判断是否存在
         * 3:如果账户不存在注册用户
         * 4:生成token
         * 5：存入redis，并返回
         * 6：加上事务，一旦有任何问题则回滚
         */
        String account = ssoParams.getAccount();
        String password = ssoParams.getPassword();
        String nickname = ssoParams.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                ||StringUtils.isBlank(nickname)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIT.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //填充属性
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);
        //生成token，存入rides
        String token = JwtUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
