package com.git.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.dto.role.RoleDTO;
import com.git.blog.dto.role.RoleQueryDTO;
import com.git.blog.dto.role.RoleVO;
import com.git.blog.dto.model.entity.Role;

import java.util.List;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
public interface RoleService {
    /**
     * 新增角色
     * @param roleDTO
     * @return
     */
    Boolean save(RoleDTO roleDTO);

    /**
     * 修改角色
     * @param roleDTO
     * @return
     */
    Boolean update(RoleDTO roleDTO);

    /**
     * 角色查询
     * @param roleQueryDTO
     * @return
     */
    List<RoleVO> list(RoleQueryDTO roleQueryDTO);

    /**
     * 角色分页查询
     * @param roleQueryDTO
     * @return
     */
    Page<RoleVO> page(RoleQueryDTO roleQueryDTO);

    /**
     * 根据id
     * @param roleId
     * @return
     */
    Boolean delete(Long roleId);

    /**
     * 根据id获取详情
     * @param roleId
     * @return
     */
    RoleVO detail(Long roleId);


    /**
     * 根据用户id获取角色菜单
     * @param uid
     * @return
     */
    List<RoleVO> getRoleListByUid(Long uid);

    /**
     * 获取管理员的角色,这个必须有，不然报错
     * @return
     */
    List<Role> getAdmin();
}
