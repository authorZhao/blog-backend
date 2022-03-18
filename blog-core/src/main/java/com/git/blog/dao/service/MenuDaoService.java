package com.git.blog.dao.service;

import com.git.blog.dto.model.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
public interface MenuDaoService extends IService<Menu> {

    /**
     * 根据菜单名字获取菜单
     * @param menuName
     * @return
     */
    Menu getByName(String menuName);
}
