package com.git.blog.service;


import com.git.blog.dto.blog.BlogArticleDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author authorZhao
 * @since 2022-03-17
 */
public interface CacheService {

    /**
     * 存
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     */
    void setStr(String key, Object value, Long time, TimeUnit timeUnit);

    /**
     * 存
     * @param key
     * @param value
     */
    default void setStr(String key, Object value){
        setStr(key,value,null,null);
    }

    /**
     * 取
     * @param key
     * @return
     */
    String getStr(String key);

    /**
     * 清除缓存
     * @param key
     */
    void remove(String key);

    /**
     * 获取系统的tag和type
     * @param key
     * @return
     */
    List<TagTypeCountDTO> getTagType(String key);
}
