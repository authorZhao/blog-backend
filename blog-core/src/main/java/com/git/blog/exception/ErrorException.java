package com.git.blog.exception;

/**
 * 业务异常
 * @author authorZhao
 * @since 2021-01-20
 */
public class ErrorException extends RuntimeException{

    /**
     * 通用异常描述与
     */
    public static final ErrorException COMMON_ERROR_EXCEPTION = new ErrorException("业务异常");

    public ErrorException() {
        super();
    }

    public ErrorException(String message) {
        super(message);
    }

    public ErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorException(Throwable cause) {
        super(cause);
    }

    protected ErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
