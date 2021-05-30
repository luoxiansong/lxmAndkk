package com.cqkk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BlogMvcConfig implements WebMvcConfigurer {

    @Autowired
    private BlogInterceptor blogInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index").setViewName("login");
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(blogInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/tologin", "/user/login", "/static/**", "/templates/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
