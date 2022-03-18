package com.git.blog.commmon.enums;

import lombok.Getter;

/**
 * 通知类型,新增组合类型
 * @author authorZhao
 * @since 2021-01-12
 */
@Getter
public enum NoticeTypeEnum {

    DING_DING(10,"钉钉"),
    WX_WORK(20,"企业微信"),
    WX_WORK_DING_DING(1000,"企业微信并且钉钉");

    private Integer value;
    private String desc;

    NoticeTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 是不是企业微信
     * @param noticeType
     * @return
     */
    public static boolean isWxWork(Integer noticeType) {
        return WX_WORK.value.equals(noticeType) || WX_WORK_DING_DING.value.equals(noticeType);
    }

    /**
     * 是不是钉钉
     * @param noticeType
     * @return
     */
    public static boolean isDingDing(Integer noticeType) {
        return DING_DING.value.equals(noticeType) || WX_WORK_DING_DING.value.equals(noticeType);
    }
}
