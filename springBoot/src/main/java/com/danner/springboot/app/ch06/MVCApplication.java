package com.danner.springboot.app.ch06;


import com.danner.springboot.app.ch06.controller.interceptor.PerformanceInteceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * WebMvcConfigurer 配置拦截器等
 * /coffee/** 接口
 */
@Slf4j
@SpringBootApplication
public class MVCApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(MVCApplication.class);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PerformanceInteceptor())
                .addPathPatterns("/coffee/**");
    }
}
