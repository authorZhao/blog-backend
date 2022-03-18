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
@ConfigurationProperties(prefix = "open.blog.wx")
public class WxProperties {

    /**
     * 微信appId
     */
    private String wxAppId;

    /**
     * wxAppSecret
     */
    private String wxAppSecret;

    /**
     * 登录回调登录页面
     */
    private String redirectLoginPage;

    /**
     * 登录回调用户信息编辑页面
     */
    private String redirectUserPage;

    /**
     * 企业微信机器人全局key
     */
    private String globeWxWorkKey;
}
