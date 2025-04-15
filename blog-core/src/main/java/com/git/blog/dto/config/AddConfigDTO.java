package com.git.blog.dto.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// AddConfigDTO.java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AddConfigDTO {
    @NotBlank
    private String name;
    private String desc;
    @NotBlank private String service;
    @NotBlank private String content;
    private Integer type;
}


