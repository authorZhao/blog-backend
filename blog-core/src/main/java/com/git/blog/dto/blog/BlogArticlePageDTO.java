package com.git.blog.dto.blog;

import com.git.blog.commmon.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlogArticle对象", description="文章表")
public class BlogArticlePageDTO extends PageParam {

    /**
    * 文章标题
    */
    @ApiModelProperty(value = "文章标题")
    private String title;

    /**
    * 用户ID
    */
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
    * 状态
    */
    @ApiModelProperty(value = "状态")
    private Integer status;


    /**
    * 是否原创 0是 10转载 20翻译
    */
    @ApiModelProperty(value = "是否原创 0是 10转载 20翻译")
    private Integer original;


    /**
    * 文章关键字，优化搜索
    */
    @ApiModelProperty(value = "文章关键字，优化搜索")
    private String keywords;


    /**
    * 该文章是否登录后才可访问0不是 1是
    */
    @ApiModelProperty(value = "该文章是否登录后才可访问0不是 1是")
    private Integer requiredAuth;

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
