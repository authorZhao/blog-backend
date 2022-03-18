package com.git.blog.exception;

/**
 * 业务异常
 * @author authorZhao
 * @since 2021-01-20
 */
public class BizException extends RuntimeException{

    /**
     * 通用异常描述与
     */
    public static final BizException COMMON_BIZ_EXCEPTION = new BizException("业务异常");

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }

    protected BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
