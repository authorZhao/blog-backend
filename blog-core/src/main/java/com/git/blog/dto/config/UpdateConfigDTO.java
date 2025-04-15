package com.git.blog.dto.config;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

// UpdateConfigDTO.java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateConfigDTO {
    @NotNull
    private Integer id;
    private String desc;
    private String content;
    private Integer type;
}
