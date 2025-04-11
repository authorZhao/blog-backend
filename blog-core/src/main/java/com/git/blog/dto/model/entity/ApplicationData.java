package com.git.blog.dto.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author authorZhao
 * @since 2025-04-11
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlogArticle对象", description="文章表")
public class ApplicationData {
    private int id;
    private String name;
    private String data;
    private String desc;
    private int version;
    private String tag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
