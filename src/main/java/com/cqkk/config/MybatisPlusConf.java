package com.cqkk.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: lxmAndkk
 * @description: mybatisplus配置 配置方言
 * @author: luo kk
 * @create: 2021-06-10 19:56
 */
@Configuration
public class MybatisPlusConf {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //设置方言类型
        paginationInterceptor.setDbType(DbType.MYSQL);
        return paginationInterceptor;
    }
}