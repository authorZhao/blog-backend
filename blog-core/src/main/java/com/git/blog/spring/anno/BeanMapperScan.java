package com.git.blog.spring.anno;

import com.git.blog.spring.BeanMapperSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置bean复制的扫描注解
 * @author authorZhao
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(BeanMapperSelector.class)
public @interface BeanMapperScan {

    /**
     * 包名
     * 例如 com.git
     * @return
     */
    String[] basePackages() default {};

    /**
     *
     * @return
     */
    Class<?>[] classes() default {};
}
