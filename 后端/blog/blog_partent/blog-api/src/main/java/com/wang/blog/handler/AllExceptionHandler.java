package com.wang.blog.handler;

import com.wang.blog.vo.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 王家俊
 * 对加了@ControllerAdvice此注解的方法进行拦截处理AOP的实现
 */
@ControllerAdvice
public class AllExceptionHandler {
    //处理Exception.class的异常
   @ExceptionHandler(Exception.class)
   @ResponseBody//返回Json数据
   public Object doException(Exception ex){
       String failMsg = null;
       if (ex instanceof MethodArgumentNotValidException){
           //拿到异常数据信息提示
           failMsg  = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError().getDefaultMessage();
       }else {
           ex.printStackTrace();
           return Result.fail(-999,"系统发生异常");
       }
       return failMsg;
   }
}
