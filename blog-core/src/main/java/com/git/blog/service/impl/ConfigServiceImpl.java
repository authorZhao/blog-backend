package com.git.blog.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.dao.service.ConfigDaoService;
import com.git.blog.dto.config.*;
import com.git.blog.dto.model.entity.ConfigEntity;
import com.git.blog.exception.BizException;
import com.git.blog.service.ConfigService;
import com.git.blog.service.map.DataConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private ConfigDaoService configDaoService;


    @Override
    public KvData updateKV(StrKV strKV) {
        PropertySource<?> refreshConfig = context.getEnvironment().getPropertySources().get("refreshConfig");
        if (refreshConfig == null) {
            refreshConfig = new MapPropertySource("refreshConfig", new HashMap<>());
        }
        Object property = refreshConfig.getProperty(strKV.key());
        if (Objects.equals(property, strKV.value())) {
            return KvData.nullUpdate(strKV);
        }
        Map source = (Map) refreshConfig.getSource();
        source.put(strKV.key(), strKV.value());

        refreshConfig = new MapPropertySource("refreshConfig", source);
        context.getEnvironment().getPropertySources().addFirst(refreshConfig);

        Thread.ofVirtual().start(() -> {
            context.publishEvent(new EnvironmentChangeEvent(Set.of(strKV.key())));
            refreshEndpoint.refresh();
            log.info("配置刷新key={}", strKV.key());
            //context.publishEvent(new RefreshEvent(ConfigServiceImpl.this, null, "Refresh Nacos config"));
        });
        return new KvData(new StrKV(strKV.key(), Optional.ofNullable(property).map(Object::toString).orElse(null)), strKV);
    }

    @Override
    public Map<String, String> updateMap(Map<String, String> map) {
        PropertySource<?> refreshConfig = context.getEnvironment().getPropertySources().get("refreshConfig");
        if (refreshConfig == null) {
            refreshConfig = new MapPropertySource("refreshConfig", new HashMap<>());
        }
        Map source = (Map) refreshConfig.getSource();
        var keys = new HashSet<String>();

        for (var entry : map.entrySet()) {
            Object o = source.get(entry.getKey());
            if (o == null) {
                keys.add(entry.getKey());
                source.put(entry.getKey(), entry.getValue());
            }
        }
        source.putAll(map);

        refreshConfig = new MapPropertySource("refreshConfig", source);
        context.getEnvironment().getPropertySources().addFirst(refreshConfig);

        Thread.ofVirtual().start(() -> {
            context.publishEvent(new RefreshEvent(ConfigServiceImpl.this, null, "Refresh Nacos config"));
            log.info("配置刷新keys={}", keys);
        });
        return source;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addConfig(AddConfigDTO dto) {
        ConfigEntity entity = new ConfigEntity()
                .setName(dto.getName())
                .setDesc(dto.getDesc())
                .setService(dto.getService())
                .setContent(dto.getContent())
                .setType(dto.getType())
                .setVersion(1)
                .setStatus((byte) 0)
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        return configDaoService.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(UpdateConfigDTO dto) {
        ConfigEntity exist = configDaoService.getById(dto.getId());
        if (exist == null) {
            throw BizException.DATA_NOT_EXITS;
        }
        LocalDateTime now = LocalDateTime.now();
        // 构建历史记录
        List<HistoryConfigItem> history = new ArrayList<>();
        if (StringUtils.isNotBlank(exist.getHistory())) {
            history = JSON.parseArray(exist.getHistory(), HistoryConfigItem.class);
        }
        history.add(new HistoryConfigItem(exist.getVersion(), exist.getContent(), now));
        if (history.size() > 10) {
            history = history.stream().sorted(Comparator.comparingInt(HistoryConfigItem::version))
                    .collect(Collectors.toList()).subList(0, 10);
        }
        return configDaoService.updateConfig(new ConfigEntity()
                .setId(dto.getId())
                .setDesc(dto.getDesc())
                .setContent(dto.getContent())
                .setType(dto.getType())
                .setVersion(exist.getVersion())
                .setHistory(JSON.toJSONString(history))
                .setUpdateTime(now));
    }

    @Override
    public boolean logicDelete(Integer id) {
        return configDaoService.logicDelete(id);
    }

    @Override
    public ConfigDetailDTO getDetail(Integer id) {
        ConfigEntity entity = configDaoService.getById(id);
        return DataConvert.INSTANCE.entityToDetailDTO(entity);
    }

    @Override
    public Page<ConfigPageDTO> getPage(ConfigPageQueryDTO dto) {
        return configDaoService.getPage(dto, DataConvert.INSTANCE::entityToPageDTO);
    }


}
