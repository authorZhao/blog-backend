package com.git.blog.dto.blog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文章类型表
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlogArticleTypes对象", description="文章类型表")
public class BlogArticleTypesDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    /**
    * 标签表主键
    */
    @ApiModelProperty(value = "标签表主键")
    private Long typeId;

    @ApiModelProperty(value = "类型名称")
    private String name;

    /**
    * 文章ID
    */
    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    /**
    * 添加时间
    */
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
