package com.git.blog.dao.service;

import com.git.blog.dto.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
public interface RoleDaoService extends IService<Role> {

    /**
     * 根据角色名字获取角色
     * @param roleName
     * @return
     */
    Role getByName(String roleName);
}
