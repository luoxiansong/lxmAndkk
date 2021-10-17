package com.cqkk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: lxmAndkk
 * @description: 项目启动类
 * @author: luo kk
 * @create: 2021-05-30 13:40
 */
@SpringBootApplication
@Transactional
@MapperScan("com.cqkk.mapper")
public class PsnproApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsnproApplication.class, args);
    }

}
