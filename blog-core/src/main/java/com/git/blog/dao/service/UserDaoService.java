package com.git.blog.dao.service;

import com.git.blog.dto.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
public interface UserDaoService extends IService<User> {

    /**
     * 根据openId获取用户
     * @param wxOpenId
     * @return
     */
    User getByWxOPenId(String wxOpenId);

    /**
     * 某用户是不是某角色
     * @param userId
     * @param roleName
     * @return
     */
    boolean isRoleByName(Long userId, String roleName);
}
