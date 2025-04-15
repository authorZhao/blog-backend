package com.git.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.dto.config.*;

import java.util.Map;

/**
 * @author authorZhao
 * @since 2025-04-09
 */
public interface ConfigService {

    /**
     *
     * @param strKV
     * @return
     */
    KvData updateKV(StrKV strKV);
    Map<String, String> updateMap(Map<String, String> map);
    boolean addConfig(AddConfigDTO dto);
    boolean updateConfig(UpdateConfigDTO dto);
    boolean logicDelete(Integer id);
    ConfigDetailDTO getDetail(Integer id);
    Page<ConfigPageDTO> getPage(ConfigPageQueryDTO dto);
}
