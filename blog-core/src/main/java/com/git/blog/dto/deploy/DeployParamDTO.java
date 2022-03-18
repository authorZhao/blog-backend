package com.git.blog.dto.deploy;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 部署对象
 * @author authorZhao
 * @since 2021-03-12
 */
@Data
@Accessors(chain = true)
public class DeployParamDTO {

    /**
     * 命名空间 "namespace":"act-data",
     */
    private String namespace;

    /**
     * 微服务名称"name":"act-data-trans-sta",
     */
    private String name;


    /**
     * 集成版本号，默认"version":"release-20200129-00",f5620
     */
    private String version;

    /**
     * 部署参数
     */
    private DeployParam params;


    @Data
    public static class DeployParam {
        /**
         * git仓库地址"scmUrl":"http://git.shangshi360.com/act-data/act-data-trans-sta.git",
         */
        private String scmUrl;
        /**
         * 模块路径"modulePath":"notice-sender-boot/notice-sender-sms",
         */
        private String modulePath;
        /**
         * git分支"repoBranch":"master",
         */
        private String repoBranch;
        /**
         * "deploymentPrefix":"notice-sender-sms",
         */
        private String deploymentPrefix;
        /**
         * 随机字符串，直接uuid
         */
        private String callbackkey;
        /**
         * 环境 cd-dev、cd-test、cd-pre、cd-blue、cd-red、cd-prod、cd-canary
         */
        private List<String> envOptions;

    }
}
