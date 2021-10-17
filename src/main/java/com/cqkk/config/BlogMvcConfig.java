package com.cqkk.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @program: lxmAndkk
 * @description: BlogMvcConfig配置
 * @author: luo kk
 * @create: 2021-05-30 13:40
 */
@Configuration
public class BlogMvcConfig implements WebMvcConfigurer {

    @Autowired
    private BlogInterceptor blogInterceptor;

  /*  @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("admin/login.html");
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(blogInterceptor).addPathPatterns("/admin/**", "/singer/**", "/apply5/*", "/sendMessage/*")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login.html");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE")
                .maxAge(168000)
                .allowedHeaders("*")
                .allowCredentials(true);
    }


}
