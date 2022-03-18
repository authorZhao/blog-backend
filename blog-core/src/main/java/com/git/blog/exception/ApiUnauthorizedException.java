package com.git.blog.exception;


import lombok.Data;

import java.io.Serializable;

/**
 * 401授权失败业务异常，过期或者没有权限
 * @author chenjin
 */
@Data
public class ApiUnauthorizedException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final ApiUnauthorizedException TOKEN_EXPIRE = new ApiUnauthorizedException("token失效或者已过期，请重新登录");
    public static final ApiUnauthorizedException NO_PERMISSION = new ApiUnauthorizedException("没有权限，请联系管理员");

    public ApiUnauthorizedException(String message) {
        super(message);
    }

    public ApiUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiUnauthorizedException(Throwable cause) {
        super(cause);
    }

    protected ApiUnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
