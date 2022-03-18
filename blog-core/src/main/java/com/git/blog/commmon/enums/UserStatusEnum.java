package com.git.blog.commmon.enums;

import lombok.Getter;

/**
 * 通用状态枚举 0，正常，1禁用
 * @author authorZhao
 * @since 2021-01-12
 */
@Getter
public enum UserStatusEnum {

    NORMAL(0,"正常"),
    DISABLE(1,"禁用"),
    PENDING_APPROVAL(2,"待审核");


    private Integer value;
    private String desc;

    UserStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
