package com.git.blog.dto.cache;

import com.alibaba.fastjson2.JSON;

/**
 * @author authorZhao
 * @since 2025-04-18
 */
public record CacheDTO(String key,Object value,long ttl,long cacheTime) {
    public static CacheDTO noTtl(String key,Object v) {
        return new CacheDTO(key,v,0,0);
    }

    public static CacheDTO strCacheDTO(String key,Object value,long ttl,long cacheTime) {
        if(value instanceof String str) {
            return new CacheDTO(key,value,ttl,cacheTime);
        }else {
            return new CacheDTO(key, JSON.toJSONString(value),ttl,cacheTime);
        }
    }

    public boolean isExpire(){
        return isExpire(System.currentTimeMillis());
    }

    public boolean isExpire(long now){
        return ttl > 0 && now > ttl + cacheTime;
    }
}
