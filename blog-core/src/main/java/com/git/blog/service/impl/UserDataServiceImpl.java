package com.git.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.blog.commmon.enums.StatusEnum;
import com.git.blog.dao.service.*;
import com.git.blog.dto.model.entity.Role;
import com.git.blog.dto.model.entity.User;
import com.git.blog.dto.model.entity.UserRole;
import com.git.blog.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2021-01-29
 */

@Service
@Slf4j
public class UserDataServiceImpl implements UserDataService {

    @Autowired
    private UserDaoService userDaoService;
    @Autowired
    private RoleDaoService roleDaoService;
    @Autowired
    private UserRoleDaoService userRoleDaoService;
    @Autowired
    private MenuDaoService menuDaoService;
    @Autowired
    private RoleMenuDaoService roleMenuDaoService;


    private static final String ADMIN = "admin";
    /**
     * 获取所有管理员，管理员不能随便创建
     * @return
     */
    @Override
    public  List<User> getAdmin() {
        // TODO 后续考虑管理员等直接缓存
        List<Role> adminList = getRoleAdmin();

        List<UserRole> list = userRoleDaoService.list(new LambdaQueryWrapper<UserRole>().in(UserRole::getRoleId, adminList.stream().map(Role::getRoleId)
                .collect(Collectors.toList())));
        List<User> users = userDaoService.listByIds(list.stream().map(UserRole::getUid).collect(Collectors.toList()));
        return users;
    }

    @Override
    public Map<Long, User> getUserMapByUid(List<Long> uidList) {
        if(CollectionUtils.isEmpty(uidList)){
            return new HashMap<>();
        }

        List<User> users = userDaoService.listByIds(uidList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList()));
        if(CollectionUtils.isEmpty(users)){
            return new HashMap<>();
        }
        return users.stream().filter(i-> StatusEnum.NORMAL.getValue().equals(i.getStatus())).collect(Collectors.toMap(User::getUid, Function.identity()));
    }

    @Override
    public boolean isAdmin(Long uid) {
        List<User> admin = getAdmin();
        return admin!=null && admin.stream().anyMatch(i->i.getUid().equals(uid));
    }

    @Override
    public List<Role> getRoleAdmin() {
        return roleDaoService.list(new LambdaQueryWrapper<Role>().eq(Role::getRoleName,ADMIN));
    }
}
