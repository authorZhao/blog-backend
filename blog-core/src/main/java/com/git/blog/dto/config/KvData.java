package com.git.blog.dto.config;

/**
 * @author authorZhao
 * @since 2025-04-09
 */
public record KvData(StrKV oldData,StrKV newData) {
    public static KvData nullUpdate(StrKV strKV) {
        return new KvData(strKV, null);
    }
}
