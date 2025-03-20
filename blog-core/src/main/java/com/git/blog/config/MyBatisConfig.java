package com.git.blog.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author authorZhao
 * @since 2020-12-25
 */

@Configuration
@MapperScan("com.git.blog.dao.mapper")
public class MyBatisConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor masterPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        new PageI
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
