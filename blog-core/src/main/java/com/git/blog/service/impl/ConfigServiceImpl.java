package com.git.blog.service.impl;

import com.git.blog.dto.config.KvData;
import com.git.blog.dto.config.StrKV;
import com.git.blog.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
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
    private Environment env;


    @Override
    public KvData updateKV(StrKV strKV) {
        PropertySource<?> refreshConfig = context.getEnvironment().getPropertySources().get("refreshConfig");
        if(refreshConfig == null) {
            Map<String, Object> map = new HashMap<>();
            refreshConfig = new MapPropertySource("refreshConfig", map);
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
            context.publishEvent(new RefreshEvent(this, null, "Refresh Nacos config"));
        });

        return new KvData(new StrKV(strKV.key(), Optional.ofNullable(property).map(Object::toString).orElse(null)),strKV);
    }
}
