package com.git.blog.commmon.enums;

import lombok.Getter;

/**
 * 组乐行枚举
 * @author authorZhao
 * @since 2021-01-12
 */
@Getter
public enum GroupTypeEnum {

    BACK_END_DEV_GROUP(0,"后端开发组"),
    FRONT_END_DEVELOPMENT_Group(1,"前端开发组"),
    OPERATION_GROUP(2,"运维"),
    QA_GROUP(3,"QA"),
    ADMINISTRATORS_GROUP(4,"管理员组");

    private Integer value;
    private String desc;

    GroupTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
