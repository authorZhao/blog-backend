package com.git.blog.api.config;

import com.git.blog.service.BlogFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.IOException;

/**
 * mvc配置
 * @author authorZhao
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private BlogFileService blogFileService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/configuration/ui",
                        "/configuration/security",
                        "/api/doc",
                        "/swagger-resources/**",
                        "/druid/**",
                        "/webjars/**",
                        "/v2/**",
                        "/git/version",
                        "/error",
                        "/data-check/**",
                        "/csrf",
                        "/swagger-ui.html",
                        "/**/favicon.ico",
                        "/api/ok",
                        "/api/check/**",
                        "/api/version",
                        "/api/user/login",
                        "/api/user/getUrl",
                        "/spi/**",
                        "/"+blogFileService.getMidPath()+"**"
                );
        //registry.addInterceptor(new TraceIdInterceptor()).addPathPatterns("/**");
        //super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        System.out.println("file:"+blogFileService.getFilePrePath());
        registry.addResourceHandler("/"+blogFileService.getMidPath()+"**").addResourceLocations("file:"+blogFileService.getFilePrePath()+blogFileService.getMidPath());
    }



}
