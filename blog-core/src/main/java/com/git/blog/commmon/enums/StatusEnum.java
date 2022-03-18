package com.git.blog.commmon.enums;

import lombok.Getter;

/**
 * 通用状态枚举 0，正常，1禁用
 * @author authorZhao
 * @since 2021-01-12
 */
@Getter
public enum StatusEnum {

    /**
     * 含义一种是正常，一种是普通的0
     */
    NORMAL(0,"正常"),
    /**
     * 含义一种是正常，一种是普通的1
     */
    DISABLE(1,"禁用");


    private Integer value;
    private String desc;

    StatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
