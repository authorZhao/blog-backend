package com.git.blog.commmon.enums;

import lombok.Getter;

/**
 * 通用配置枚举
 * @author authorZhao
 * @since 2021-01-12
 */
@Getter
public enum ConfigStatusEnum {
//0申请中，1已配置（成功），2配置失败
    IN_APPLYING(0,"申请中"),
    SUCCESS(1,"配置成功"),
    ERROR(2,"配置报错");

    private Integer value;
    private String desc;

    ConfigStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
