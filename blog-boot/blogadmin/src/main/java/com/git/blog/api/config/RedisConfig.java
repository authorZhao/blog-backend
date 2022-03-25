package com.git.blog.api.config;

import com.git.blog.service.CacheService;
import com.git.blog.service.impl.LocalCacheServiceImpl;
import com.git.blog.service.impl.RedisCacheServiceImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @date 2022-02-28
 * @author authorZhao
 */
@Configuration
public class RedisConfig {
    @Configuration
    @ConditionalOnProperty(value = "spring.data.redis.enabled",havingValue = "false")
    @SpringBootApplication(exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
    public static class CloseRedisSwitch{
        @Bean
        public CacheService cacheService(){
            return new LocalCacheServiceImpl();
        }

    }

    @Configuration
    @ConditionalOnProperty(value = "spring.data.redis.enabled",havingValue = "true")
    public static class OpenRedisSwitch{
        @Bean
        public CacheService cacheService(){
            return new RedisCacheServiceImpl();
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.setKeySerializer(RedisSerializer.string());
            redisTemplate.setValueSerializer(RedisSerializer.string());
            redisTemplate.setHashKeySerializer(RedisSerializer.string());
            redisTemplate.setHashValueSerializer(RedisSerializer.string());
            return redisTemplate;
        }
    }
}
