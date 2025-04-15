package com.git.blog.dto.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// ConfigPageDTO.java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigPageDTO {
    private Integer id;
    private String name;
    private String desc;
    private String service;
    private Integer version;
    private Integer type;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
