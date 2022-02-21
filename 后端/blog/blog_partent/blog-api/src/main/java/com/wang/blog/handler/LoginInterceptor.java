package com.wang.blog.handler;

import com.alibaba.fastjson.JSON;
import com.wang.blog.dao.pojo.SysUser;
import com.wang.blog.service.LoginService;
import com.wang.blog.utils.UserThreadLocal;
import com.wang.blog.vo.ErrorCode;
import com.wang.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 王家俊
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行方法执行之前进行执行
        /**
         * 1.需要判断请求的路径是否为 HandlerMethod(controller方法)
         * 1.判断token是否为空，如果为空 未登录
         * 2.如果token不为空 ，登陆验证loginservice checkToken
         * 3.如果认证成功，即放行
         */
        if (!(handler instanceof HandlerMethod)){
            //如果访问的不是controller方法那就直接放行
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        //String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser ==null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(),ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //登陆验证成功，放行
        //希望在controller中直接获取用户数据
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除,ThreadLocal中用完的信息会有内存泄漏的风险
        UserThreadLocal.remove();
    }
}
