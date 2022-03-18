package com.git.blog.commmon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : chendaqing
 * @date : 2019/6/12 10:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="PageParam分页对象", description="分页对象")
public class PageParam {

    /**
     * 当前页码,默认填1
     */
    @ApiModelProperty(name = "current", value = "当前页码,默认填1")
    private Integer current = 1;

    /**
     * 每页显示记录数,默认填10
     */
    @ApiModelProperty(name = "pageSize", value = "每页显示记录数,默认填10")
    private Integer pageSize = 10;

}
