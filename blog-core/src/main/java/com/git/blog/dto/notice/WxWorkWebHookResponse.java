package com.git.blog.dto.notice;

import lombok.Data;

import java.io.Serializable;

/**
 * 企业维信通知返回结果
 */
@Data
public class WxWorkWebHookResponse implements Serializable {

    private static final long serialVersionUID = 1111L;

    private Integer errcode;
    private String errmsg;

    public boolean isOk() {
        return new Integer(0).equals(errcode);
    }

}
