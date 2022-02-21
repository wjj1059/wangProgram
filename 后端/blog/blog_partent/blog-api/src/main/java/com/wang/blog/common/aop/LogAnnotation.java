package com.wang.blog.common.aop;

import java.lang.annotation.*;

/**
 * @author 王家俊
 * Type 代表可以放在类上面 Mehod表示可以放在方法上
 *
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operator() default "";
}
