package com.git.blog.dto.model.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.git.blog.commmon.CommonString;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章分类表
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlogType对象", description="文章分类表")
public class BlogType implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long pid;

    /**
    * 文章类型名
    */
    @ApiModelProperty(value = "文章类型名")
    private String name;

    /**
    * 类型介绍
    */
    @ApiModelProperty(value = "类型介绍")
    private String description;

    /**
    * 排序
    */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
    * 图标
    */
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
    * 状态
     * @see CommonString.TYPE_DELETE_STATUS
    */
    @ApiModelProperty(value = "状态")
    private Integer status;

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
