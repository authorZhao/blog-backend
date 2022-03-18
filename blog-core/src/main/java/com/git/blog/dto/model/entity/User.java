package com.git.blog.dto.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="User对象", description="用户表")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
    * uid
    */
    @ApiModelProperty(value = "uid")
    @TableId(value = "uid", type = IdType.AUTO)
    private Long uid;

    /**
    * 用户名
    */
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 用户微信头像
     */
    @ApiModelProperty(value = "用户微信头像")
    private String avatar;

    /**
    * 用户状态，0正常，1禁用，2待审核，待审核的状态可以查询用户信息
    */
    @ApiModelProperty(value = "用户状态，0正常，1禁用，2待审核")
    private Integer status;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 盐
     */
    @ApiModelProperty(value = "盐")
    private String salt;


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
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    public String getUsername() {
        return StringUtils.isBlank(username)?nickname:username;
    }

}
