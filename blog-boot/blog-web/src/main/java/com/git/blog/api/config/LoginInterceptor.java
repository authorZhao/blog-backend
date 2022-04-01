package com.git.blog.api.config;

import com.alibaba.fastjson.JSON;
import com.git.blog.commmon.ApiResponse;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.properties.SysProperties;
import com.git.blog.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author authorZhao
 * @since 2020-12-30
 */
@Slf4j
@Configuration
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final String token = "token";
    @Autowired
    private SysProperties sysProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(BooleanUtils.isFalse(sysProperties.getNeedLogin())){
            return super.preHandle(request,response,handler);
        }
        String header = request.getHeader(token);
        JwtUtil.TokenEntity tokenEntity = null;
        if (StringUtils.isBlank(header)) {
            return unauthorizedException(response);
        }
        tokenEntity = JwtUtil.verifyToken(header,sysProperties.getSecretKey());
        if (tokenEntity==null || tokenEntity.isFail()) {
            return unauthorizedException(response);
        }

        String uid = JwtUtil.getClaim(header);
        if(StringUtils.isBlank(uid)){
            return unauthorizedException(response);
        }
        AuthTheadLocal.set(Long.valueOf(uid));
        return super.preHandle(request,response,handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthTheadLocal.remove();
        super.afterCompletion(request,response,handler,ex);
    }


    /**
     * token验证失败直接返回前端
     * @param response
     * @return
     */
    private boolean unauthorizedException(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null ;
        try{
            ApiResponse<Object> apiResponse = ApiResponse.error("登录凭证不合法或已过期")
                    .setCode(401);
            out = response.getWriter();
            out.append(JSON.toJSONString(apiResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
}
