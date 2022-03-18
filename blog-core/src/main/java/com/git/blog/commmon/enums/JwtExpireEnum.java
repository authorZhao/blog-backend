package com.git.blog.commmon.enums;

/**
 * JWT过期时间枚举
 * @author 吴伟鑫
 */
public enum JwtExpireEnum {
    /**
     * 毫秒
     */
    MIllI_SECOND(),
    /**
     * 秒
     */
    SECOND(),
    /**
     * 分钟
     */
    MINUTE(),
    /**
     * 小时
     */
    HOUR(),
    /**
     * 天
     */
    DAY();
}
