package com.git.blog.dto.deploy;

import lombok.Data;

/**
 * @author authorZhao
 * @since 2021-03-18
 */
@Data
public class DeployResponse {

    /**
     * 状态码
     */
    private String code;

    /**
     * msg
     */
    private String msg;

    /**
     * token
     */
    private String token;

    /**
     * 下次重置时间
     */
    private String resetTime;

}
