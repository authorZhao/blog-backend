package com.git.blog.config;

import java.lang.annotation.*;

/**
 * 按钮权限，只有拥有某个按钮的才能执行某个方法
 * @author authorZhao
 * @since 2021-01-22
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Permission {

    /**
     * 是否放行
     * @return
     */
    boolean open() default true;

    /**
     * 不配置使用springmvc的路径，配置按照配置来
     * @return
     */
    String url() default "";

    /**
     * 默认提示
     * @return
     */
    String message() default "";

}
