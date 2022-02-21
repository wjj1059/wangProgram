package com.wang.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wang.blog.dao.mapper.SysUserMapper;
import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.service.LoginService;
import com.wang.blog.service.SysUserService;
import com.wang.blog.vo.ErrorCode;
import com.wang.blog.vo.LoginUserVo;
import com.wang.blog.vo.Result;
import com.wang.blog.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * @author 王家俊
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Lazy
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser ==null){
            sysUser = new SysUser();
            sysUser.setNickname("小王同学");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        //where查询
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         * 2.是否为空，解析是否为空redis中是否存在
         * 3.如果校验失败，返回错误
         * 4.如果成功，返回相应的结果 LoginVo
         */

       SysUser sysUser = loginService.checkToken(token);
       if (sysUser==null){
         return  Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
       }
       LoginUserVo loginUserVo = new LoginUserVo();
       loginUserVo.setId(String.valueOf(sysUser.getId()));
       loginUserVo.setNickname(sysUser.getNickname());
       loginUserVo.setAvatar(sysUser.getAvatar());
       loginUserVo.setAccount(sysUser.getAccount());
       return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户这 id自动生成
        //这个地方默认生成的id是分布式id ，采用雪花算法
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if (sysUser ==null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("小王同学");
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(sysUser,vo);
        vo.setId(String.valueOf(sysUser.getId()));
        return vo;
    }
}
