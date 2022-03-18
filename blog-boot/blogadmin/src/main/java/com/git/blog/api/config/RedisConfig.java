package com.git.blog.api.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @date 2022-02-28
 * @author authorZhao
 */
@Configuration
public class RedisConfig {



    @Configuration
    @ConditionalOnProperty(value = "spring.data.redis.enabled",havingValue = "false")
    @SpringBootApplication(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
    public static class OpenRedisSwitch{

    }

    @Configuration
    @ConditionalOnProperty(value = "spring.data.redis.enabled",havingValue = "true")
    public static class CloseRedisSwitch{

    }
}
