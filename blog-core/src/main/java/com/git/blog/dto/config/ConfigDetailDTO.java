package com.git.blog.dto.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

// ConfigDetailDTO.java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigDetailDTO {
    private Integer id;
    private String name;
    private String desc;
    private String service;
    private String content;
    private Integer version;
    private List<HistoryConfigItem> historyList;
    private Integer type;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
