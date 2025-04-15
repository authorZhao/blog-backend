package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.git.blog.dao.mapper.ConfigMapper;
import com.git.blog.dao.service.ConfigDaoService;
import com.git.blog.dto.config.ConfigPageQueryDTO;
import com.git.blog.dto.model.entity.ConfigEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Slf4j
public class ConfigDaoServiceImpl extends ServiceImpl<ConfigMapper, ConfigEntity> implements ConfigDaoService {

    @Override
    public boolean logicDelete(Integer id) {
        var wrapper = new LambdaUpdateWrapper<ConfigEntity>()
                .eq(ConfigEntity::getId, id).set(ConfigEntity::getStatus, 1);
        return update(wrapper);
    }

    @Override
    public boolean updateConfig(ConfigEntity configEntity) {
        var version = configEntity.getVersion();
        var wrapper = new LambdaUpdateWrapper<ConfigEntity>()
                .eq(ConfigEntity::getId, configEntity.getId()).eq(ConfigEntity::getVersion, version);
        configEntity.setVersion(version + 1);
        return update(configEntity, wrapper);
    }

    @Override
    public Page<ConfigEntity> getPage(ConfigPageQueryDTO dto) {
        var wrapper = new LambdaQueryWrapper<ConfigEntity>()
                .select(ConfigEntity::getId, ConfigEntity::getVersion, ConfigEntity::getName,
                        ConfigEntity::getType, ConfigEntity::getService, ConfigEntity::getStatus,
                        ConfigEntity::getDesc, ConfigEntity::getCreateTime, ConfigEntity::getUpdateTime
                )
                .eq(ConfigEntity::getStatus, 0)
                .like(StringUtils.isNotBlank(dto.getService()), ConfigEntity::getService, dto.getService())
                .eq(dto.getVersion() != null, ConfigEntity::getVersion, dto.getVersion())
                .like(StringUtils.isNotBlank(dto.getName()), ConfigEntity::getName, dto.getName());

        return page(new Page<>(dto.getCurrent(), dto.getPageSize()), wrapper);
    }
}
