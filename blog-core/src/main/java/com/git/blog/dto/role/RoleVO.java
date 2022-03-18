package com.git.blog.dto.role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.git.blog.dto.model.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Role对象", description="角色表")
public class RoleVO implements Serializable {

    private static final long serialVersionUID=1L;

    /**
    * 角色ID
    */
    @ApiModelProperty(value = "角色ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
    * 角色名称
    */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
    * 角色描述
    */
    @ApiModelProperty(value = "角色描述")
    private String remark;

    /**
    * 角色状态，0正常，1禁用
    */
    @ApiModelProperty(value = "角色状态，0正常，1禁用")
    private Integer status;

    /**
    * 创建者用户uid
    */
    @ApiModelProperty(value = "创建者用户uid")
    private Long createUid;

    /**
     * 创建者用户uid
     */
    @ApiModelProperty(value = "创建者用户名")
    private String createUserName;

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

    /**
     * 菜单列表
     */
    @ApiModelProperty(value = "菜单列表")
    private List<Menu> menuList;
}
