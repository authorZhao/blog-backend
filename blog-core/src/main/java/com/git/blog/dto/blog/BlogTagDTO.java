package com.git.blog.dto.blog;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 标签表，作为二级分类直接与uid关联
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlogTag对象", description="标签表，作为二级分类直接与uid关联")
public class BlogTagDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    /**
    * 书签名
    */
    @ApiModelProperty(value = "书签名")
    private String name;

    /**
    * 描述
    */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
    * 创建者
    */
    @ApiModelProperty(value = "创建者")
    private Long createUid;

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
