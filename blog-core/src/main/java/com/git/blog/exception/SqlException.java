package com.git.blog.exception;


import lombok.Data;

import java.io.Serializable;

/**
 * sql异常
 * @author chenjin
 */
@Data
public class SqlException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sql解析异常，一般是指语法异常，未执行
     */
    public static final SqlException SQL_PARSE_EXCEPTION = new SqlException("sql解析异常");

    public static final SqlException SQL_EXECUTE_EXCEPTION = new SqlException("sql执行异常");

    public SqlException(String message) {
        super(message);
    }

    public SqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlException(Throwable cause) {
        super(cause);
    }

    protected SqlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
