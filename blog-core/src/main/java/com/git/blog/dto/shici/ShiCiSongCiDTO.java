package com.git.blog.dto.shici;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@ApiModel(value="ShiCiSongCi对象", description="")
public class ShiCiSongCiDTO implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    /**
    * 作者
    */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
    * 段落内容
    */
    @ApiModelProperty(value = "段落内容")
    private String paragraphs;

    /**
    * 词牌名
    */
    @ApiModelProperty(value = "词牌名")
    private String rhythmic;

    /**
     * type
     */
    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "内容")
    private List<String> paragraphList;
}
