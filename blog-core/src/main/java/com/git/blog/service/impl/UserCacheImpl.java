package com.git.blog.service.impl;

import com.git.blog.service.UserCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author authorZhao
 * @since 2021-01-25
 */
@Service
@Slf4j
public class UserCacheImpl implements UserCache {

    private static final Cache<String, String> localCache = Caffeine.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();


    @Override
    public <T> T get(String key,Class<T> clazz) {
        Object ifPresent = localCache.getIfPresent(key);
        System.out.println("走了缓存ifPresent = " + ifPresent);
        return (T) ifPresent;
    }

    @Override
    public void set(String key, Object value) {
        localCache.put(key,value.toString());
    }

    @Override
    public void remove(String key) {
        localCache.invalidate(key);
    }

    @Override
    public void removeAll() {
        localCache.invalidateAll();
    }
}
