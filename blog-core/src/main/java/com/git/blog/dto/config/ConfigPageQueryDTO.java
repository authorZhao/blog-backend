package com.git.blog.dto.config;

import com.git.blog.commmon.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

// ConfigPageDTO.java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigPageQueryDTO extends PageParam {
    private String service;
    private Integer version;
    private String name;
}
