package com.git.blog.service.impl;

import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.service.CacheService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Override
    public void setStr(String key, Object value, Long time, TimeUnit timeUnit) {
        LOCAL_CACHE.put(key, value.toString());
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
