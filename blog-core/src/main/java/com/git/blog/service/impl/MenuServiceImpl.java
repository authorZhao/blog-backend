package com.git.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.enums.StatusEnum;
import com.git.blog.dao.service.MenuDaoService;
import com.git.blog.dao.service.RoleMenuDaoService;
import com.git.blog.dao.service.UserDaoService;
import com.git.blog.dto.menu.MenuDTO;
import com.git.blog.dto.menu.MenuQueryDTO;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.menu.MenuVO;
import com.git.blog.exception.BizException;
import com.git.blog.dto.model.entity.Menu;
import com.git.blog.dto.model.entity.RoleMenu;
import com.git.blog.dto.model.entity.User;
import com.git.blog.service.MenuService;
import com.git.blog.service.bean.AuthBeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {


    /**
     * 所有的菜单树
     */
    private static final String MENU_TREE_LIST_ALL = "MENU_TREE_LIST_ALL";

    @Autowired
    private MenuDaoService menuDaoService;
    @Autowired
    private AuthBeanMapper authBeanMapper;
    @Autowired
    private UserDaoService userDaoService;
    @Autowired
    private RoleMenuDaoService roleMenuDaoService;

    /**
     * 根菜单id
     */
    private static final Long ROOT_MENU_ID = 1L;

    @Override
    public Boolean save(MenuDTO menuDTO) {
        //1.校验：数据、用户
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO,menu);
        menu.setStatus(StatusEnum.NORMAL.getValue());
        //名字重复校验
        Menu menu1 = menuDaoService.getByName(menu.getMenuName());
        if(menu1!=null) {
            throw new RuntimeException("菜单名字重复");
        }
        clearCache();
        return menuDaoService.save(menu);
    }

    @Override
    public Boolean update(MenuDTO menuDTO) {
        //1.校验：数据、用户
        Menu byId = menuDaoService.getById(menuDTO.getMenuId());
        if(byId==null){
            throw new BizException("数据不存在");
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO,menu);
        Menu menu1 = menuDaoService.getByName(menu.getMenuName());
        if(menu1!=null && !menu1.getMenuId().equals(byId.getMenuId())) {
            throw new BizException("菜单名字重复");
        }
        clearCache();
        return menuDaoService.updateById(menu);
    }

    @Override
    public List<MenuVO> list(MenuQueryDTO menuQueryDTO) {
        List<MenuVO> menuVOList = new ArrayList<>();
        List<Menu> list = menuDaoService.list(new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(menuQueryDTO.getMenuName()), Menu::getMenuName, menuQueryDTO.getMenuName()));
        if(CollectionUtils.isNotEmpty(list)){
            menuVOList = list.stream().map(authBeanMapper::convertMenuToVO).collect(Collectors.toList());
        }
        return menuVOList;
    }

    @Override
    public Page treeList(MenuQueryDTO menuQueryDTO) {
        Page<Menu> page = new Page(menuQueryDTO.getCurrent(), menuQueryDTO.getPageSize());
        page = menuDaoService.page(page, new LambdaQueryWrapper<Menu>()
                .gt(Menu::getMenuId,ROOT_MENU_ID)
                .like(StringUtils.isNotBlank(menuQueryDTO.getMenuName()), Menu::getMenuName, menuQueryDTO.getMenuName())
                //默认只展示一级节点
                .eq(StringUtils.isBlank(menuQueryDTO.getMenuName()), Menu::getMenuParentId, ROOT_MENU_ID)
                .eq(Menu::getStatus, StatusEnum.NORMAL.getValue() )
        );
        if(CollectionUtils.isEmpty(page.getRecords())){
            return new Page();
        }

        //确定0位根节点
        List<Menu> listAll = menuDaoService.list(new LambdaQueryWrapper<Menu>().eq(Menu::getStatus, StatusEnum.NORMAL.getValue() ));
        List<Menu> list = page.getRecords();
        if(CollectionUtils.isEmpty(list)){
            return new Page<>();
        }
        Set<Long> uidSet = list.stream().map(Menu::getCreateUid).collect(Collectors.toSet());
        List<User> users = userDaoService.listByIds(uidSet);
        Map<Long, User> collect = new HashMap<>();
        if(CollectionUtils.isNotEmpty(users)){
            collect = users.stream().collect(Collectors.toMap(User::getUid, Function.identity()));
        }
        final Map<Long, User> userMap = collect;


        List<MenuTreeVO> menuTreeVOList = listAll.stream().map(i->{
            MenuTreeVO menuTreeVO = authBeanMapper.convertMenuToMenuTreeVO(i);
            menuTreeVO.setCreateUserName(Optional.ofNullable(userMap.get(i.getCreateUid())).map(User::getUsername).orElse(StringUtils.EMPTY));
            return menuTreeVO;
        }).collect(Collectors.toList());

        List<MenuTreeVO> pageList = list.stream().map(i->{
            MenuTreeVO menuTreeVO = authBeanMapper.convertMenuToMenuTreeVO(i);
            menuTreeVO.setCreateUserName(Optional.ofNullable(userMap.get(i.getCreateUid())).map(User::getUsername).orElse(StringUtils.EMPTY));
            return menuTreeVO;
        }).collect(Collectors.toList());

        pageList.forEach(i->genTreeMap(i,menuTreeVOList));

        //从根节点往下遍历
        Page page2 = page;
        page2.setRecords(pageList);
        return page2;
    }

    @Override
    public List<MenuTreeVO> treeListAll() {

        List<MenuTreeVO> treeListAll = null;
                //userCache.getList(MENU_TREE_LIST_ALL,MenuTreeVO.class);
        if(CollectionUtils.isNotEmpty(treeListAll)){
            return treeListAll;
        }
        Menu root = getRootMenu();
        List<Menu> list = menuDaoService.list(new LambdaQueryWrapper<Menu>().eq(Menu::getStatus, StatusEnum.NORMAL.getValue() ));
        if(root==null||CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        Set<Long> uidSet = list.stream().map(Menu::getCreateUid).collect(Collectors.toSet());
        List<User> users = userDaoService.listByIds(uidSet);
        Map<Long, User> collect = new HashMap<>();
        if(CollectionUtils.isNotEmpty(users)){
            collect = users.stream().collect(Collectors.toMap(User::getUid, Function.identity()));
        }
        final Map<Long, User> userMap = collect;

        List<MenuTreeVO> menuTreeVOList = list.stream().map(i->{
            MenuTreeVO menuTreeVO = authBeanMapper.convertMenuToMenuTreeVO(i);
            menuTreeVO.setCreateUserName(Optional.ofNullable(userMap.get(i.getCreateUid())).map(User::getUsername).orElse(StringUtils.EMPTY));
            return menuTreeVO;
        }).collect(Collectors.toList());
        MenuTreeVO menuTreeVO = authBeanMapper.convertMenuToMenuTreeVO(root);
        //从根节点往下遍历
        genTreeMap(menuTreeVO,menuTreeVOList);
        //userCache.set(MENU_TREE_LIST_ALL,menuTreeVO.getChildren());
        return menuTreeVO.getChildren();
    }

    /**
     * 生成菜单树
     * @param menuTreeVO
     * @param menuTreeVOList
     */
    @Override
    public void genTreeMap(MenuTreeVO menuTreeVO, List<MenuTreeVO> menuTreeVOList) {
        menuTreeVOList.stream().filter(i->menuTreeVO.getMenuId().equals(i.getMenuParentId())).forEach(
                i->{
                    List<MenuTreeVO> children = menuTreeVO.getChildren();
                    if(children==null){
                        children = new ArrayList<>();
                        menuTreeVO.setChildren(children);
                    }
                    children.add(i);
                    genTreeMap(i,menuTreeVOList);
                }
        );
    }

    @Override
    public Page page(MenuQueryDTO menuQueryDTO) {
        Page<Menu> page = new Page(menuQueryDTO.getCurrent(), menuQueryDTO.getPageSize());
         page = menuDaoService.page(page, new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(menuQueryDTO.getMenuName()), Menu::getMenuName, menuQueryDTO.getMenuName()));
         if(CollectionUtils.isNotEmpty(page.getRecords())){
             List collect = page.getRecords().stream().map(authBeanMapper::convertMenuToVO).collect(Collectors.toList());
             page.setRecords(collect);
         }
        return page;
    }

    @Override
    public Boolean delete(Long menuId) {
        //校验权限
        Menu byId = menuDaoService.getById(menuId);
        if(byId==null){
            return false;
        }
        int count = menuDaoService.count(new LambdaQueryWrapper<Menu>().eq(
                Menu::getMenuParentId, byId.getMenuId()));
        if(count>0){
            throw new BizException("该菜单存在子节点，不能直接删除");
        }
        //该菜单有角色绑定也不能删除
        List<RoleMenu> roleMenuList = roleMenuDaoService.list(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId,menuId));
        if(CollectionUtils.isNotEmpty(roleMenuList)){
            throw new BizException("该菜单存在角色绑定，不能直接删除");
        }

        byId.setStatus(StatusEnum.DISABLE.getValue());
        byId.setUpdateTime(new Date());
        clearCache();
        return menuDaoService.removeById(byId);
    }

    @Override
    public MenuVO detail(Long menuId) {
        //权限校验
        MenuVO menuVO = new MenuVO();

        //1.
        Menu byId = menuDaoService.getById(menuId);
        if(byId!=null){
            BeanUtils.copyProperties(byId,menuVO);
        }
        return menuVO;
    }

    @Override
    public Menu getRootMenu() {
        return menuDaoService.getById(ROOT_MENU_ID);
    }

    /*@Override
    public Menu getAdmin() {
        return menuDaoService.getByName(ADMIN);
    }*/

    public void clearCache(){
        //userCache.remove(MENU_TREE_LIST_ALL);
    }
}
