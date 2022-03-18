package com.git.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author authorZhao
 * @since 2020-12-28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "open.blog.sys")
public class SysProperties {

    /**
     * jwt的Token
     */
    private String secretKey;

    /**
     * jwt的过期时间，单位小时
     */
    private Integer expireTime;

    /**
     * 是否需要登录
     */
    private Boolean needLogin;

    /**
     * 后台权限开关
     */
    private Boolean permission = true;

    /**
     * 前端域名
     */
    private String frontUrl;

    /**
     * 部署之后的回调地址
     */
    private String deployCallbackUrl;

    /**
     * jenKins用户名
     */
    private String jenKinsUsername;

    /**
     * jenKins密码
     */
    private String jenKinsPassword;

    private String imgHost = "http://localhost:8080/";

    /**是否使用hexo静态页面*/
    private Boolean hexoUse = Boolean.TRUE;

    /**是否使用hexo静态页面*/
    private String hexoPath = "E:/idea/workspace2/blogs/hexo";

}
