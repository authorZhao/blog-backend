package com.git.blog.commmon.enums;

import lombok.Getter;

/**
 * 微服务审核状态
 * @author authorZhao
 * @since 2020-12-25
 */
@Getter
public enum MSApplyStatusEnum {

    PENDING_APPROVAL(0,"待审核"),
    REJECT(1,"驳回修改"),
    APPROVED(2,"审核通过"),
    FAIL_APPROVAL(3,"审核不通过");


    private Integer value;
    private String desc;

    MSApplyStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
