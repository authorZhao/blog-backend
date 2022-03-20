package com.git.blog.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author authorZhao
 * @since 2020-12-29
 */
//@Configuration
public class FilterConfig {


    /**
     * xss
     * @return
     */
    @Bean
    public FilterRegistrationBean xssRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(Integer.MAX_VALUE-1);

        return filterRegistrationBean;
    }


}
