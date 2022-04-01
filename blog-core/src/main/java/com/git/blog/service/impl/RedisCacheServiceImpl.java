package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.blog.commmon.CommonString;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.blog.TagTypeCountDTO;
import com.git.blog.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author authorZhao
 * @since 2022-03-24
 */

@Slf4j
public class RedisCacheServiceImpl implements CacheService {

    @Autowired
    private BlogTypeDaoService blogTypeDaoService;

    @Autowired
    private BlogTagDaoService blogTagDaoService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final Long TIME_OUT = 30L;

    @Override
    public void setStr(String key, Object value, Long time, TimeUnit timeUnit) {
        if(time==null){
            time = TIME_OUT;
        }
        if(timeUnit==null) {
            timeUnit = TimeUnit.MINUTES;
        }
        if(StringUtils.isEmpty(key)){
            return;
        }
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value),time,timeUnit);
    }

    @Override
    public String getStr(String key) {
        if(StringUtils.isEmpty(key)){
            return null;
        }
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }

}
