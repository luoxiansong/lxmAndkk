package com.cqkk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @program: lxmAndkk
 * @description: Swagger2配置类
 * @author: luo kk
 * @create: 2021-05-31 21:22
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //控制开启或关闭swagger
    @Value("${swagger.enabled}")
    private Boolean swaggerEnabled;

    @Bean
    public Docket createApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                //api基础信息
                .apiInfo(apiInfo())
                //控制或开启swagger
                .enable(swaggerEnabled)
                //选择哪些路径和api会生成document
                .select()
                //扫描展示api的路径包
                .apis(RequestHandlerSelectors.basePackage("com.cqkk.controller"))
                //对所有路径进行监控
                .paths(PathSelectors.any())
                //构建
                .build();
    }

    /**
     * @descripiton:
     * @author: luo kk
     * @date: 2021/5/31 10:33
     * @return：springfox.documentation.service.ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //名称
                .title("Swagger API")
                //api描述
                .description("person boot project")
                //api版本
                .version("1.0")
                //构建
                .build();
    }
}
