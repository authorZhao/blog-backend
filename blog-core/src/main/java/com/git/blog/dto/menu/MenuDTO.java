package com.git.blog.dto.menu;

import com.git.blog.commmon.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

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
public class MenuDTO extends PageParam implements Serializable {

    private static final long serialVersionUID=1L;

    /**
    * 菜单/按钮ID
    */
    @ApiModelProperty(value = "菜单/按钮ID")
    private Long menuId;

    /**
    * 上级菜单ID
    */
    @ApiModelProperty(value = "上级菜单ID")
    @NotNull(message = "上级菜单id不可为空")
    private Long menuParentId;

    /**
    * 菜单/按钮名称
    */
    @ApiModelProperty(value = "菜单/按钮名称")
    @NotBlank(message = "菜单名称不可为空")
    private String menuName;

    /**
     * 0不展示(隐藏)，1展示，2不是组件
     */
    @ApiModelProperty(value = "0不展示(隐藏)，1展示，2不是组件")
    private Integer isShow;

    /**
    * 菜单URL
    */
    @ApiModelProperty(value = "菜单URL")
    private String menuUrl;

    /**
    * 图标
    */
    @ApiModelProperty(value = "图标")
    private String menuIcon;

    /**
    * 类型 0菜单 1按钮
    */
    @ApiModelProperty(value = "类型 0菜单 1按钮")
    @NotNull(message = "菜单类型不可为空")
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
