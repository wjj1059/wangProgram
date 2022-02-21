package com.wang.blog.config;

import com.wang.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 王家俊
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置
        registry.addMapping("/**").allowedOrigins("http://loaclhost:8080");
        //通过ip访问不能走443端口
        //如果配置了自定义拦截器，这种跨域配置会失效，所以采用第二种
        //跨域配置一
//      registry.addMapping("/**").allowedOrigins("http://81.71.87.241:8080","http://81.71.87.241:80","http://81.71.87.241:8888")
        //跨域配置二
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截test接口，后续遇到实际的路径，在配置为真正的拦截接口
        registry.addInterceptor(loginInterceptor)
                //全局拦截，放行注册和首页
                //.addPathPatterns("/**").excludePathPatterns("/login").excludePathPatterns("/register");
        .addPathPatterns("/test")
        .addPathPatterns("/articles/publish")
        .addPathPatterns("/comments/create/change");

    }
    //    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor)
//                .addPathPatterns("/test")
//                .addPathPatterns("/comments/create/change")
//                .addPathPatterns("/articles/publish");
//
//    }
    }

