package com.git.blog.dto.wx;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class WxAccessTokenRspDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    private Integer errcode;
    private String errmsg;




    public boolean isSuccess(){
        return StringUtils.isBlank(errmsg);
    }

    public boolean isError(){
        return StringUtils.isNotBlank(errmsg);
    }
}
