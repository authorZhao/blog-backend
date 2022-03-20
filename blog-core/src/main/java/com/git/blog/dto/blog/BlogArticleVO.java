package com.git.blog.dto.blog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
public class BlogArticleVO implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

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

    @ApiModelProperty(value = "作者")
    private String author;

    /**
    * 文章封面图片，英文逗号分隔，最多3
    */
    @ApiModelProperty(value = "文章封面图片，英文逗号分隔，最多3")
    private String coverImage;

    /**
    * 当前文章适用的编辑器类型0md，10富文本
    */
    @ApiModelProperty(value = "当前文章适用的编辑器类型0md，10富文本")
    private Integer editorType;

    /**
    * 文章专属二维码地址
    */
    @ApiModelProperty(value = "文章专属二维码地址")
    private String qrcodePath;

    /**
    * 文章内容
    */
    @ApiModelProperty(value = "文章内容")
    private String content;

    /**
    * markdown版的文章内容
    */
    @ApiModelProperty(value = "markdown版的文章内容")
    private String contentMd;

    /**
    * 是否置顶,0不是 1是
    */
    @ApiModelProperty(value = "是否置顶,0不是 1是")
    private Integer top;

    /**
    * 状态
    */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
    * 是否推荐0不是 1是
    */
    @ApiModelProperty(value = "是否推荐0不是 1是")
    private Integer recommended;

    /**
    * 是否原创 0是 10转载 20翻译
    */
    @ApiModelProperty(value = "是否原创 0是 10转载 20翻译")
    private Integer original;

    /**
    * 文章简介，最多200字
    */
    @ApiModelProperty(value = "文章简介，最多200字")
    private String description;

    /**
    * 文章关键字，优化搜索
    */
    @ApiModelProperty(value = "文章关键字，优化搜索")
    private String keywords;

    /**
    * 是否开启评论0不是 1是
    */
    @ApiModelProperty(value = "是否开启评论0不是 1是")
    private Integer comment;

    /**
    * 文章私密访问时的密钥
    */
    @ApiModelProperty(value = "文章私密访问时的密钥")
    private String password;

    /**
    * 该文章是否登录后才可访问0不是 1是
    */
    @ApiModelProperty(value = "该文章是否登录后才可访问0不是 1是")
    private Integer requiredAuth;

    /**
    * 转载或者翻译的原文地址url
    */
    @ApiModelProperty(value = "转载或者翻译的原文地址url")
    private String reprintUrl;

    @ApiModelProperty(value = "标签多个,用英文逗号分隔")
    private String tags;

    @ApiModelProperty(value = "分类多个,用英文逗号分隔")
    private String types;

    @ApiModelProperty(value = "标签id")
    private List<Long> tagIds;

    @ApiModelProperty(value = "分了id")
    private List<Long> typeIds;

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
