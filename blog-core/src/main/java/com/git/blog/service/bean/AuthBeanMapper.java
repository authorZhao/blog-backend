package com.git.blog.service.bean;

import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.menu.MenuVO;
import com.git.blog.dto.role.RoleVO;
import com.git.blog.dto.user.UserDTO;
import com.git.blog.dto.model.entity.*;

/**
 * 用户角色菜单之间的bean转化
 * @author authorZhao
 * @since 2020-12-25
 */
public interface AuthBeanMapper {


    /**
     * 菜单实体和菜单返回对象之间转化
     * @param menu
     * @return
     */
    MenuVO convertMenuToVO(Menu menu);

    MenuTreeVO convertMenuToMenuTreeVO(Menu menu);

    User convertUserDTOToUser(UserDTO userDTO);

    /**
     * 角色实体转化
     * @param role
     * @return
     */
    RoleVO convertRoleToRoleVO(Role role);


}
