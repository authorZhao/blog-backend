package com.git.blog.spring.asm;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author authorZhao
 * @since 2020-12-28
 */
@Data
public class MethodGetSet{
    /**
     * 字段名
     */
    private String field;

    /**
     * get的方法名
     */
    private String getMethod;

    /**
     * set方法名
     */
    private String setMethod;

    /**
     * get方法
     */
    private Method methodGet;

    /**
     * set方法
     */
    private Method methodSet;
}
