package com.git.blog;

import com.git.blog.spring.anno.BeanMapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author authorZhao
 * @since 2020-12-24
 */


@SpringBootApplication(scanBasePackages = {"com.git.blog"} )
@BeanMapperScan(basePackages = "com.git.blog.service.bean")
@MapperScan("com.git.blog.dao.mapper")
@EnableFeignClients(basePackages = "com.git.blog.feign")
@EnableAsync
public class BlogWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogWebApplication.class,args);
    }
}
