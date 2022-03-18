package com.git.blog.service;

import com.git.blog.dto.model.entity.Role;
import com.git.blog.dto.model.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户数据服务
 * @author authorZhao
 * @since 2021-01-29
 */
public interface UserDataService {

    /**
     * 获取管理员
     * @return
     */
    List<User> getAdmin();

    /**
     * 根据uid获取userMap
     * @param uidList 用户uid集合，获取不到禁用用户
     * @return
     */
    Map<Long,User> getUserMapByUid(List<Long> uidList);

    /**
     * 是不是管理员
     * @param uid
     * @return boolean
     */
    boolean isAdmin(Long uid);

    /**
     * 获取管理员角色
     * @return
     */
    List<Role> getRoleAdmin();

}
