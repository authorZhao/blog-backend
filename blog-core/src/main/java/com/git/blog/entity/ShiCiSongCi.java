package com.git.blog.entity;

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
@TableName("shi_ci_song_ci")
@ApiModel(value="ShiCiSongCi对象", description="")
public class ShiCiSongCi implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
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
}
