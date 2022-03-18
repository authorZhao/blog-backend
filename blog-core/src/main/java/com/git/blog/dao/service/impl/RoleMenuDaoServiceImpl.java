package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.blog.dao.service.MenuDaoService;
import com.git.blog.dto.model.entity.Menu;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.RoleMenu;
import com.git.blog.dao.mapper.RoleMenuMapper;
import com.git.blog.dao.service.RoleMenuDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Service
@Slf4j
public class RoleMenuDaoServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuDaoService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private MenuDaoService menuDaoService;



    @Override
    public void saveOrUpdateDelete(Long roleId, List<Long> menuIds) {
        menuIds = menuIds.stream().distinct().collect(Collectors.toList());
        //新菜单，避免忽略前端
        List<Menu> menus = menuDaoService.listByIds(menuIds);
        if(CollectionUtils.isEmpty(menus)){
            menus = new ArrayList<>();
        }
        List<Long> newMenus = menus.stream().map(Menu::getMenuId).collect(Collectors.toList());
        //旧菜单
        List<RoleMenu> list1 = list(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        List<Long> oldMenus = list1.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());


        //1.新增,直接用菜单id
        List<Long> addList = newMenus.stream().filter(i -> !oldMenus.contains(i)).collect(Collectors.toList());

        //2.删除
        List<Long> deleteList = list1.stream().filter(i -> !newMenus.contains(i.getMenuId())).map(RoleMenu::getId).collect(Collectors.toList());

        if(CollectionUtils.isNotEmpty(deleteList)){
            removeByIds(deleteList);
        }

        if(CollectionUtils.isNotEmpty(addList)){
            List<RoleMenu> collect = addList.stream().map(i -> new RoleMenu().setRoleId(roleId).setMenuId(i)
            ).collect(Collectors.toList());
            saveBatch(collect);
        }

    }
}
