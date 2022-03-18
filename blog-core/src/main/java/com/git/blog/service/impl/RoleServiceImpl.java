package com.git.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.enums.StatusEnum;
import com.git.blog.dao.service.*;
import com.git.blog.dto.role.RoleDTO;
import com.git.blog.dto.role.RoleQueryDTO;
import com.git.blog.dto.role.RoleVO;
import com.git.blog.exception.BizException;
import com.git.blog.dto.model.entity.*;
import com.git.blog.service.MenuService;
import com.git.blog.service.RoleService;
import com.git.blog.service.bean.AuthBeanMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDaoService roleDaoService;
    @Autowired
    private RoleMenuDaoService roleMenuDaoService;
    @Autowired
    private MenuDaoService menuDaoService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserRoleDaoService userRoleDaoService;
    @Autowired
    private AuthBeanMapper authBeanMapper;
    @Autowired
    private UserDaoService userDaoService;

    @Override
    public Boolean save(RoleDTO roleDTO) {
        //1.校验，设置uid

        //2.去重

        //3.新增角色

        List<Long> menuIds = roleDTO.getMenuIds();
        if(CollectionUtils.isEmpty(menuIds)){
            return false;
        }
        menuIds = menuIds.stream().distinct().collect(Collectors.toList());
        List<Menu> menus = menuDaoService.listByIds(menuIds);
        //角色夏季而传
        if(CollectionUtils.isEmpty(menuIds) || menuIds.size()!=menus.size()){
            return false;
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO,role);
        role.setStatus(StatusEnum.NORMAL.getValue());
        Role role1 = roleDaoService.getByName(role.getRoleName());
        if(role1!=null){
            throw new BizException("角色名重复");
        }
        roleDaoService.save(role);

        List<RoleMenu> collect = menuIds.stream().map(i -> new RoleMenu().setRoleId(role.getRoleId()).setMenuId(i)
        ).collect(Collectors.toList());
        roleMenuDaoService.saveBatch(collect);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(RoleDTO roleDTO) {

        //1.校验
        if(Objects.isNull(roleDTO) || Objects.isNull(roleDTO.getRoleId())){
            return false;
        }

        Role byId = roleDaoService.getById(roleDTO.getRoleId());
        if(byId==null){
            return false;
        }
        //修改角色
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO,role);
        role.setUpdateTime(new Date());

        Role role1 = roleDaoService.getByName(role.getRoleName());
        if(role1!=null && !role1.getRoleId().equals(role.getRoleId())){
            throw new BizException("角色名重复");
        }
        roleDaoService.updateById(role);

        List<Long> menuIds = roleDTO.getMenuIds();

        //这里更改策略，不在笼统的删除，避免roleMenu表自增id增长过快
        roleMenuDaoService.saveOrUpdateDelete(roleDTO.getRoleId(),menuIds);

        return true;
    }

    @Override
    public List<RoleVO> list(RoleQueryDTO roleQueryDTO) {
        List<Role> list = roleDaoService.list(new LambdaQueryWrapper<Role>()
                .eq(Role::getStatus,0)
                .like(StringUtils.isNotBlank(roleQueryDTO.getRoleName()), Role::getRoleName, roleQueryDTO.getRoleName())

        );
        if(CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        Set<Long> uidSet = list.stream().map(Role::getCreateUid).collect(Collectors.toSet());
        List<User> users = userDaoService.listByIds(uidSet);
        Map<Long, User> collect = new HashMap<>();
        if(CollectionUtils.isNotEmpty(users)){
            collect = users.stream().collect(Collectors.toMap(User::getUid, Function.identity()));
        }
        final Map<Long, User> userMap = collect;

        List roleVOList = list.stream().map(i -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(i, roleVO);
            roleVO.setCreateUserName(Optional.ofNullable(userMap.get(i.getCreateUid())).map(User::getUsername).orElse(StringUtils.EMPTY));
            return roleVO;
        }).collect(Collectors.toList());
        return roleVOList;
    }

    @Override
    public Page page(RoleQueryDTO roleQueryDTO) {
        Page<Role> page = new Page(roleQueryDTO.getCurrent(), roleQueryDTO.getPageSize());
        page = roleDaoService.page(page, new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(roleQueryDTO.getRoleName()), Role::getRoleName, roleQueryDTO.getRoleName()));
        if(CollectionUtils.isEmpty(page.getRecords())){
            return new Page();
        }


        Set<Long> uidSet = page.getRecords().stream().map(Role::getCreateUid).collect(Collectors.toSet());
        List<User> users = userDaoService.listByIds(uidSet);
        Map<Long, User> collect = new HashMap<>();
        if(CollectionUtils.isNotEmpty(users)){
            collect = users.stream().collect(Collectors.toMap(User::getUid, Function.identity()));
        }
        final Map<Long, User> userMap = collect;


        List roleVOList = page.getRecords().stream().map(i -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(i, roleVO);
            roleVO.setCreateUserName(Optional.ofNullable(userMap.get(i.getCreateUid())).map(User::getUsername).orElse(StringUtils.EMPTY));
            return roleVO;
        }).collect(Collectors.toList());
        page.setRecords(roleVOList);

        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long roleId) {
        //1.权限校验

        List<UserRole> userRoleList = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId));
        if(CollectionUtils.isNotEmpty(userRoleList)){
            throw new BizException("该角色存在用户绑定，不能直接删除");
        }

        //2.删除role-menu
        roleMenuDaoService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId,roleId));
        //3.删除role
        return roleDaoService.removeById(roleId);
    }

    @Override
    public RoleVO detail(Long roleId) {
        RoleVO roleVO = new RoleVO();

        //权限校验
        Role byId = roleDaoService.getById(roleId);
        if(Objects.isNull(byId)){
            return roleVO;

        }
        BeanUtils.copyProperties(byId,roleVO);
        List<RoleMenu> list = roleMenuDaoService.list(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        if(CollectionUtils.isEmpty(list)){
            return roleVO;
        }
        //菜单查询
        List<Long> menuIds = list.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        List<Menu> menus = menuDaoService.listByIds(menuIds);
        roleVO.setMenuList(menus);
        return roleVO;
    }


    @Override
    public List<RoleVO> getRoleListByUid(Long uid) {
        List<RoleVO> list = new ArrayList<>();
        List<UserRole> userRoleList = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid, uid));
        if(CollectionUtils.isEmpty(userRoleList)){
            return list;
        }
        List<Long> roleIds = userRoleList.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());
        List<Role> roleList = roleDaoService.listByIds(roleIds);
        if(CollectionUtils.isEmpty(roleList)){
            return list;
        }

        list = roleList.stream().map(authBeanMapper::convertRoleToRoleVO).distinct().collect(Collectors.toList());

        list.forEach(i->{
            List<RoleMenu> roleMenuList = roleMenuDaoService.list(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, i.getRoleId()));
            if(CollectionUtils.isEmpty(roleMenuList)){
                return;
            }
            List<Long> menuIdList = roleMenuList.stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
            List<Menu> menuList = menuDaoService.listByIds(menuIdList);
            if(CollectionUtils.isNotEmpty(menuList)){
                i.setMenuList(menuList);
            }
        });
        return list;
    }

    @Override
    public List<Role> getAdmin() {
        return roleDaoService.list(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, CommonString.ROLE_DEFAULT_ADMIN));
    }
}
