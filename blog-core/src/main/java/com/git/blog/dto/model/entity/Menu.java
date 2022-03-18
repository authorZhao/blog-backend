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

/**
 * <p>
 *
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Menu对象", description="")
public class Menu implements Serializable {

    private static final long serialVersionUID=1L;

    /**
    * 菜单/按钮ID
    */
    @ApiModelProperty(value = "菜单/按钮ID")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
    * 上级菜单ID
    */
    @ApiModelProperty(value = "上级菜单ID")
    private Long menuParentId;

    /**
    * 菜单/按钮名称
    */
    @ApiModelProperty(value = "菜单/按钮名称")
    private String menuName;

    /**
    * 菜单URL
    */
    @ApiModelProperty(value = "菜单URL")
    private String menuUrl;

    /**
     * 0不展示(隐藏)，1展示，2不是组件
     */
    @ApiModelProperty(value = "0不展示(隐藏)，1展示，2不是组件")
    private Integer isShow;

    /**
    * 图标
    */
    @ApiModelProperty(value = "图标")
    private String menuIcon;

    /**
    * 类型 0菜单 1按钮
    */
    @ApiModelProperty(value = "类型 0菜单 1按钮")
    private Integer menuType;

    /**
    * 排序
    */
    @ApiModelProperty(value = "排序")
    private Long menuSort;

    /**
    * 创建者用户id
    */
    @ApiModelProperty(value = "创建者用户id")
    private Long createUid;

    /**
     * 菜单状态，0正常，1删除
     */
    @ApiModelProperty(value = "菜单状态，0正常，1删除")
    private Integer status;

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


}
