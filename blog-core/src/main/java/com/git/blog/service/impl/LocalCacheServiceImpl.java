package com.git.blog.service.impl;

import com.alibaba.fastjson2.JSON;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.cache.CacheDTO;
import com.git.blog.service.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.TimeoutUtils;

import java.awt.image.Kernel;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 去除Caffeine本地缓存,简单实现一个
 * @author authorZhao
 * @since 2022-03-17
 */
@Slf4j
public class LocalCacheServiceImpl implements CacheService {

    @Autowired
    private BlogTypeDaoService blogTypeDaoService;
    @Autowired
    private BlogTagDaoService blogTagDaoService;

    private static final Cache<String, CacheDTO> CACHE_DTO_CACHE = Caffeine.newBuilder()
            .maximumSize(80000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Override
    public void setObj(String key, Object value, Long time, TimeUnit timeUnit) {
        var ttl =  TimeoutUtils.toMillis(time, timeUnit);
        var cache = CacheDTO.strCacheDTO(key, value, ttl, System.currentTimeMillis());
        CACHE_DTO_CACHE.put(key, cache);
    }

    @Override
    public String getStr(String key) {
        var cacheDTO = CACHE_DTO_CACHE.getIfPresent(key);
        if(cacheDTO == null) {
            return null;
        }

        if(cacheDTO.isExpire()){
            CACHE_DTO_CACHE.invalidate(key);
            return null;
        }
        return Optional.ofNullable(cacheDTO.value()).map(Object::toString).orElse(null);
    }

    @Override
    public void remove(String key) {
        CACHE_DTO_CACHE.invalidate(key);
    }

    public Set<String> allKeys(){
        return CACHE_DTO_CACHE.asMap().keySet();
    }

    public Map<String,CacheDTO> allValues(){
        return CACHE_DTO_CACHE.asMap();
    }

}
