package com.git.blog.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.git.blog.dto.config.ConfigPageQueryDTO;
import com.git.blog.dto.model.entity.ConfigEntity;
import com.git.blog.util.PageUtil;

import java.util.function.Function;

public interface ConfigDaoService extends IService<ConfigEntity> {
    boolean logicDelete(Integer id);

    boolean updateConfig(ConfigEntity configEntity);

    default  <T> Page<T> getPage(ConfigPageQueryDTO dto, Function<ConfigEntity, T> function){
        return PageUtil.convert(getPage(dto), function);
    }
    Page<ConfigEntity> getPage(ConfigPageQueryDTO dto);
}
