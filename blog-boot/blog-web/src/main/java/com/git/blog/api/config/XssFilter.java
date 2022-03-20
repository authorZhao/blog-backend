package com.git.blog.api.config;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import java.io.IOException;

/**
 * @author authorZhao
 * @since 2020-12-29
 */
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
