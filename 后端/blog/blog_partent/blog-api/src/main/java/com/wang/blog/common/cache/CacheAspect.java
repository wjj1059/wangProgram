package com.wang.blog.common.cache;

import com.alibaba.fastjson.JSON;
import com.wang.blog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;


/**
 * @author 王家俊
 * aop定义一个切面 ，切面定义了切点和通知关系
 */
@Aspect
@Component
@Slf4j
public class CacheAspect {
   @Autowired
   private RedisTemplate<String, String> redisTemplate;
//切点
   @Pointcut("@annotation(com.wang.blog.common.cache.Cache)")
   public void pt(){}

//通知
   @Around("pt()")
   public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
       try {
           Signature signature = joinPoint.getSignature();
           //类名
           String className = joinPoint.getTarget().getClass().getSimpleName();
           //调用方法名
           String methodName = signature.getName();
           Class[] parameterTypes = new Class[joinPoint.getArgs().length];
           Object[] args = joinPoint.getArgs();
           //参数
           String params="";
           for (int i = 0; i < args.length; i++) {
               if (args!=null){
                   params = JSON.toJSONString(args[i]);
                   parameterTypes[i] = args[i].getClass();
               }else {
                   parameterTypes[i]=null;
               }
           }
          if (StringUtils.isNotEmpty(params)){
              //加密，以防止出现key过长以及字符转义获取不到的情况
              params = DigestUtils.md5Hex(params);
          }
           Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
          //获取Cache注解
           Cache annotation = method.getAnnotation(Cache.class);
           //缓存过期时间
           long expire = annotation.expire();
           //缓存名称
           String name = annotation.name();
           //先从redis获取
           String redisKey = name+"::"+className+"::"+methodName+""+params;
           String redisValue = redisTemplate.opsForValue().get(redisKey);
           if (StringUtils.isNotEmpty(redisValue)){
               log.info("走了缓存，{}{}",className,methodName);
               return JSON.parseObject(redisValue, Result.class);
           }
           Object proceed = joinPoint.proceed();
           redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expire));
           log.info("存入缓存~~{}{}",className,methodName);
           return proceed;
       }catch (Throwable throwable){
           throwable.printStackTrace();
       }
       return Result.fail(-888,"缓存存在异常");
   }
}
