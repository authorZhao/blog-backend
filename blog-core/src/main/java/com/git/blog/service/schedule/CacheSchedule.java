package com.git.blog.service.schedule;

import com.git.blog.dto.cache.CacheDTO;
import com.git.blog.service.CacheService;
import com.git.blog.service.impl.LocalCacheServiceImpl;
import com.git.blog.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author authorZhao
 * @since 2025-04-18
 */
@Slf4j
public class CacheSchedule {

    @Autowired
    private LocalCacheServiceImpl localCacheService;

    @Async("commonPoolExecutor")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void remove() {
        log.info("开始清理缓存");
        Map<String, CacheDTO> stringCacheDTOMap = localCacheService.allValues();
        var now = System.currentTimeMillis();
        Set<String> keys = new HashSet<>();
        stringCacheDTOMap.values().forEach(i -> {
            if(i.isExpire(now)){
                localCacheService.remove(i.key());
                keys.add(i.key());
                return;
            }
            // 设置时间比1天大，自动续期
            var glOneDay =  i.ttl() > TimeUtils.DAY_MILLI_SECOND;
            var leftTime = i.ttl() + i.cacheTime() - now;
            if(glOneDay && leftTime > 0){
                localCacheService.setObj(i.key(),i.value(), leftTime, TimeUnit.MILLISECONDS);
            }
        });
        log.info("缓存清理完毕keys={}", keys);
    }
}
