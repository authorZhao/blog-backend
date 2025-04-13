package com.git.blog.service.impl;

import com.alibaba.fastjson2.JSON;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.service.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.Kernel;
import java.util.concurrent.TimeUnit;

/**
 * @author authorZhao
 * @since 2022-03-17
 */
@Slf4j
public class LocalCacheServiceImpl implements CacheService {

    @Autowired
    private BlogTypeDaoService blogTypeDaoService;
    @Autowired
    private BlogTagDaoService blogTagDaoService;

    private static final Cache<String, String> LOCAL_CACHE = Caffeine.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    private static final Cache<String, String> object_Cache = Caffeine.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    @Override
    public void setObj(String key, Object value, Long time, TimeUnit timeUnit) {
        if(value instanceof String str) {
            LOCAL_CACHE.put(key, str);
        }else {
            LOCAL_CACHE.put(key, JSON.toJSONString(value));
        }

    }

    @Override
    public void setStr5Min(String key, String value) {
        object_Cache.put(key, value);
    }

    @Override
    public String getStr5Min(String key) {
        return object_Cache.getIfPresent(key);
    }

    @Override
    public String getStr(String key) {
        var ifPresent = LOCAL_CACHE.getIfPresent(key);
        if(ifPresent==null){
            return null;
        }
        return (String) ifPresent;
    }

    @Override
    public void remove(String key) {
        LOCAL_CACHE.invalidate(key);
    }

}
