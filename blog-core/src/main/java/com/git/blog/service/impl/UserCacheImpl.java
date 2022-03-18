package com.git.blog.service.impl;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.git.blog.config.LocalThreadFactory;
import com.git.blog.service.UserCache;
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

    private static ListeningExecutorService backgroundRefreshPools = MoreExecutors
            .listeningDecorator(
                    new ThreadPoolExecutor(3, 5,
                            0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(5),new LocalThreadFactory()));

    private static Cache<String, Object> localCache = CacheBuilder.newBuilder()
            // 最大缓存数据量
            .maximumSize(2000)
            // 初始容量
            .initialCapacity(1000)
            // 用来开启Guava Cache的统计功能
            .recordStats()
            // 过期清除
            .expireAfterWrite(5, TimeUnit.MINUTES)
            // 每隔十s缓存值则会被刷新。防止缓存穿透
            .refreshAfterWrite(3, TimeUnit.MINUTES)
            .removalListener((RemovalListener<String, Object>) notification -> {
                log.info("key expire:{}",notification.getKey());
            }).build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) throws Exception {
                    return null;
                }
                @Override
                public ListenableFuture<Object> reload(final String key,Object oldValue) throws Exception {
                    return backgroundRefreshPools.submit(() -> null);
                }
            });


    @Override
    public <T> T get(String key,Class<T> clazz) {
        Object ifPresent = localCache.getIfPresent(key);
        System.out.println("走了缓存ifPresent = " + ifPresent);
        return (T) ifPresent;
    }

    @Override
    public void set(String key, Object value) {
        localCache.put(key,value);
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
