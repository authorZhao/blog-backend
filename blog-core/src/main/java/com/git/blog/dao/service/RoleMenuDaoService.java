package com.git.blog.dao.service;

import com.git.blog.dto.model.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
public interface RoleMenuDaoService extends IService<RoleMenu> {

    /**
     * 角色才重新绑定
     * @param roleId
     * @param menuIds
     */
    void saveOrUpdateDelete(Long roleId, List<Long> menuIds);
}
