package com.git.blog.dao.service;

import com.git.blog.dto.model.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
public interface UserRoleDaoService extends IService<UserRole> {

    /**
     * 某个用户是不是该角色
     * @param uid
     * @param roleId
     * @return
     */
    boolean isRole(Long uid, Long roleId);
}
