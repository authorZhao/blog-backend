package com.git.blog.service;

import com.git.blog.dto.config.KvData;
import com.git.blog.dto.config.StrKV;
import jakarta.validation.Valid;

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
    KvData updateKV(@Valid StrKV strKV);
}
