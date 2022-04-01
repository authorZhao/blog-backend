package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.blog.commmon.CommonString;
import com.git.blog.config.LocalThreadFactory;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.blog.BlogArticleDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;
import com.git.blog.service.CacheService;
import com.git.blog.service.TagTypeService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
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
            .expireAfterWrite(30, TimeUnit.MINUTES)
            // 每隔十s缓存值则会被刷新。防止缓存穿透
            .removalListener((RemovalListener<String, Object>) notification -> {
                log.info("key expire:{}",notification.getKey());
            }).build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) throws Exception {
                    return null;
                }
                @Override
                public ListenableFuture<Object> reload(final String key, Object oldValue) throws Exception {
                    return backgroundRefreshPools.submit(() -> null);
                }
            });


    @Override
    public void setStr(String key, Object value, Long time, TimeUnit timeUnit) {
        localCache.put(key,value);
    }

    @Override
    public String getStr(String key) {
        Object ifPresent = localCache.getIfPresent(key);
        if(ifPresent==null){
            return null;
        }
        System.out.println("走了缓存ifPresent = " + ifPresent);
        return (String) ifPresent;
    }

    @Override
    public void remove(String key) {
        localCache.invalidate(key);
    }

}
