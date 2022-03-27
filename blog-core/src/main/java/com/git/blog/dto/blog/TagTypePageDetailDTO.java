package com.git.blog.dto.blog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class TagTypePageDetailDTO {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "标签类型表主键")
    private Long id;

    @ApiModelProperty(value = "标签分类名称")
    private String name;

    @ApiModelProperty(value = "博客列表")
    private Page<BlogArticleYearDTO> blogArticleYearDTOList;


}
