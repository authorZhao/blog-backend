package com.git.blog.service;

import java.util.List;

/**
 * 用户权限缓存起来，目前本地缓存,后续考虑远程缓存，避免一直查询用户菜单
 * @author authorZhao
 * @since 2021-01-25
 */
public interface UserCache {


    /**
     * 获取值
     * @param clazz
     * @param <T>
     * @param key
     * @return
     */
    <T> T get(String key,Class<T> clazz);

    /**
     * 获取list的值
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    default <T> List<T> getList(String key, Class<T> clazz){
        return get(key,List.class);
    }

    /**
     * 存值
     * @param key
     * @param value
     */
    void set(String key,Object value);

    /**
     * 清除缓存
     * @param key
     */
    void remove(String key);

    /**
     * 删除所有缓存
     */
    void removeAll();
}
