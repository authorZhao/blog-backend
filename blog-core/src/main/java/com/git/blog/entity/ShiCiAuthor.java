package com.git.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author authorZhao
 * @since 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shi_ci_author")
@ApiModel(value="ShiCiAuthor对象", description="")
public class ShiCiAuthor implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
    * 名字
    */
    @ApiModelProperty(value = "名字")
    private String name;

    /**
    * 描述
    */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
    * 简介
    */
    @ApiModelProperty(value = "简介")
    @TableField("short_description")
    private String shortDescription;


}
