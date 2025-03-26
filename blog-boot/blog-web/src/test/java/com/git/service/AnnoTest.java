package com.git.service;

import com.git.blog.api.controller.front.BlogTagController;
import jdk.jfr.AnnotationElement;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 测试spring注解的作用，好久不用熟悉
 * @author authorZhao
 * @since 2025-03-26
 */
public class AnnoTest {

    @Test
    public void test() {
        var method = Arrays.stream(BlogTagController.class.getDeclaredMethods()).filter(i->i.getName().equals("addTag")).findFirst().orElse(null);
        if(method == null) {
            return;
        }

        RequestMapping annotation = AnnotationUtils.getAnnotation(method, RequestMapping.class);
        System.out.println("annotation = " + annotation);

        Set<RequestMapping> requestMapping = AnnotatedElementUtils.findAllMergedAnnotations(method, RequestMapping.class);
        System.out.println("annotation = " + requestMapping);
    }
}
