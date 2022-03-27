package com.git.blog.dto.blog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 按照年份
 */
@Data
public class BlogArticleYearDTO{
    @ApiModelProperty(value = "年份")
    private Integer year;
    @ApiModelProperty(value = "博客列表")
    private List<BlogArticleDTO> blogArticleDTOList;
}
