package com.wang.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.service.LoginService;
import com.wang.blog.service.SysUserService;
import com.wang.blog.utils.JwtUtils;
import com.wang.blog.vo.ErrorCode;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.param.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 王家俊
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {
    @Lazy
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate redisTemplate;
    //加密盐
    private static final String slat = "wang!@#";

    /**
     * 检查参数是否合法
     * 根据用户名和密码验证去user表查询
     * 如果存在，使用jwt生成token返回给前端
     * 如果不存在，则登陆失败
     * token放入redis中，redis token:user 信息，设置过期时间
     *
     * @param loginParam
     * @return
     */
    @Override
    public Result login( @Valid LoginParam loginParam) {
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        String pwd  = DigestUtils.md5Hex(password+slat);
        SysUser sysUser = sysUserService.findUser(account, pwd);
        //Optional<SysUser> user = Optional.ofNullable(sysUser);
        if (sysUser==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JwtUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> map = JwtUtils.checkToken(token);
        if (map==null){
            return null;
        }
        String userJson =(String) redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson,SysUser.class);
        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }


}
