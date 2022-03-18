package com.git.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉相关配置
 * @author authorZhao
 * @since 2020-12-28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "open.blog.ding")
public class DingProperties {


    /**
     * 全局钉钉token
     */
    private String globeDingTalkToken;

    /**
     * 全局钉钉Secret
     */
    private String globeDingTalkSecret;

}
