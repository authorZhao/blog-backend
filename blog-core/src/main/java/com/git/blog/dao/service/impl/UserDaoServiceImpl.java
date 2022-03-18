package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.blog.dao.service.RoleDaoService;
import com.git.blog.dao.service.UserRoleDaoService;
import com.git.blog.dto.model.entity.Role;
import com.git.blog.dto.model.entity.UserRole;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.User;
import com.git.blog.dao.mapper.UserMapper;
import com.git.blog.dao.service.UserDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Service

@Slf4j
public class UserDaoServiceImpl extends ServiceImpl<UserMapper, User> implements UserDaoService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleDaoService roleDaoService;
    @Autowired
    private UserRoleDaoService userRoleDaoService;

    @Override
    public User getByWxOPenId(String wxOpenId) {
        List<User> list = list(new LambdaQueryWrapper<User>().eq(User::getPassword, wxOpenId));
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @Override
    public boolean isRoleByName(Long userId, String roleName) {
        if(userId==null || StringUtils.isBlank(roleName)){
            return false;
        }
        Role role = roleDaoService.getByName(roleName);
        if(role==null){
            return false;
        }

        int count = userRoleDaoService.count(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid,userId).eq(UserRole::getRoleId,role.getRoleId()));
        return count>0;
    }

}
