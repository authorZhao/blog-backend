package com.git.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.dto.menu.MenuDTO;
import com.git.blog.dto.menu.MenuQueryDTO;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.menu.MenuVO;
import com.git.blog.dto.model.entity.Menu;

import java.util.List;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
public interface MenuService {

    /**
     * 新增菜单
     * @param menuDTO
     * @return
     */
    Boolean save(MenuDTO menuDTO);

    /**
     * 修改菜单
     * @param menuDTO
     * @return
     */
    Boolean update(MenuDTO menuDTO);

    /**
     * 菜单查询
     * @param menuQueryDTO
     * @return
     */
    List<MenuVO> list(MenuQueryDTO menuQueryDTO);

    /**
     * 菜单树查询
     * @param menuQueryDTO
     * @return
     */
    Page<MenuTreeVO> treeList(MenuQueryDTO menuQueryDTO);

    /**
     * 菜单分页查询
     * @param menuQueryDTO
     * @return
     */
    Page<MenuVO> page(MenuQueryDTO menuQueryDTO);

    /**
     * 根据id删除
     * @param menuId
     * @return
     */
    Boolean delete(Long menuId);

    /**
     * 根据id获取详情
     * @param menuId
     * @return
     */
    MenuVO detail(Long menuId);

    /**
     * 获取所有节点
     * @return
     */
    List<MenuTreeVO> treeListAll();

    /**
     * 菜单树生成
     * @param menuTreeVO 根节点
     * @param menuTreeVOList 菜单列表
     */
    void genTreeMap(MenuTreeVO menuTreeVO, List<MenuTreeVO> menuTreeVOList);

    /**
     * 获取根菜单的方法，根菜单项目初始化的时候就存在，辅助菜单树
     * @return
     */
    Menu getRootMenu();

    /**
     * 获取管理员
     * @return
     */
    //Menu getAdmin();
}
