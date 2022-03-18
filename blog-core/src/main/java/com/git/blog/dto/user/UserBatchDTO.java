package com.git.blog.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户批量修改对象
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="用户表")
public class UserBatchDTO implements Serializable {

    private static final long serialVersionUID=1L;

    /**
    * uidList
    */
    @ApiModelProperty(value = "uidList")
    @NotEmpty(message = "用户id不可为空")
    private List<Long> uidList;


    /**
    * 用户状态，0正常，1禁用
    */
    @ApiModelProperty(value = "用户状态，0正常，1禁用")
    //@NotNull(message = "用户状态不可为空")
    private Integer status;

    /**
    * 用户介绍
    */
    @ApiModelProperty(value = "用户介绍")
    private String introduce;

    /**
    * 审核通过者uid
    */
    @ApiModelProperty(value = "审核通过者uid")
    private Long approvedUid;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private List<Long> roleIds;
}
