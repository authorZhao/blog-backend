package com.git.blog.service.impl;

import com.git.blog.dto.config.KvData;
import com.git.blog.dto.config.StrKV;
import com.git.blog.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author authorZhao
 * @since 2025-04-09
 */
@Service
@Slf4j
@RefreshScope
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private RefreshEndpoint refreshEndpoint;


    @Override
    public KvData updateKV(StrKV strKV) {
        PropertySource<?> refreshConfig = context.getEnvironment().getPropertySources().get("refreshConfig");
        if(refreshConfig == null) {
            refreshConfig = new MapPropertySource("refreshConfig", new HashMap<>());
        }
        Object property = refreshConfig.getProperty(strKV.key());
        if(Objects.equals(property, strKV.value())) {
            return KvData.nullUpdate(strKV);
        }
        Map source = (Map) refreshConfig.getSource();
        source.put(strKV.key(), strKV.value());

        refreshConfig = new MapPropertySource("refreshConfig", source);
        context.getEnvironment().getPropertySources().addFirst(refreshConfig);

        Thread.ofVirtual().start(()->{
            context.publishEvent(new EnvironmentChangeEvent(Set.of(strKV.key())));
            refreshEndpoint.refresh();
            log.info("配置刷新key={}", strKV.key());
            //context.publishEvent(new RefreshEvent(ConfigServiceImpl.this, null, "Refresh Nacos config"));
        });
        return new KvData(new StrKV(strKV.key(), Optional.ofNullable(property).map(Object::toString).orElse(null)),strKV);
    }

    @Override
    public Map<String, String> updateMap(Map<String, String> map) {
        PropertySource<?> refreshConfig = context.getEnvironment().getPropertySources().get("refreshConfig");
        if(refreshConfig == null) {
            refreshConfig = new MapPropertySource("refreshConfig", new HashMap<>());
        }
        Map source = (Map) refreshConfig.getSource();
        var keys = new HashSet<String>();

        for (var entry : map.entrySet()) {
            Object o = source.get(entry.getKey());
            if(o == null) {
                keys.add(entry.getKey());
                source.put(entry.getKey(), entry.getValue());
            }
        }
        source.putAll(map);

        refreshConfig = new MapPropertySource("refreshConfig", source);
        context.getEnvironment().getPropertySources().addFirst(refreshConfig);

        Thread.ofVirtual().start(()->{
            context.publishEvent(new RefreshEvent(ConfigServiceImpl.this, null, "Refresh Nacos config"));
            log.info("配置刷新keys={}", keys);
        });
        return source;
    }
}
