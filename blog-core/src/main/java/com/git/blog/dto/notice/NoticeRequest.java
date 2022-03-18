package com.git.blog.dto.notice;

import com.git.blog.commmon.enums.NoticeTypeEnum;

/**
 * 通知对象
 * @author authorZhao
 * @since 2021-03-11
 */
public abstract class NoticeRequest {

    /**
     * 通知类型
     * @return
     */
    public abstract NoticeTypeEnum getNoticeType();
}
