package com.git.blog.dto.blog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章标签表
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlogArticleTags对象", description="文章标签表")
public class TagTypeDetailDTO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "标签类型表主键")
    private Long id;

    @ApiModelProperty(value = "标签分类名称")
    private String name;

    @ApiModelProperty(value = "博客列表")
    private List<BlogArticleYearDTO> blogArticleYearDTOList;


    /**
     * 按照年份
     */
    @Data
    public static class BlogArticleYearDTO{
        @ApiModelProperty(value = "年份")
        private Integer year;
        @ApiModelProperty(value = "博客列表")
        private List<BlogArticleDTO> blogArticleDTOList;
    }

}
