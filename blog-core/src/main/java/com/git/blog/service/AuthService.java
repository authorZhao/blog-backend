package com.git.blog.service;

/**
 * @author authorZhao
 * @since 2022-03-06
 */
public interface AuthService {
    /**
     * 判断是有有权限
     * @param uid uid
     * @param menuName 菜单名字
     * @return
     */
    boolean checkAuth(Long uid, String menuName);
}
