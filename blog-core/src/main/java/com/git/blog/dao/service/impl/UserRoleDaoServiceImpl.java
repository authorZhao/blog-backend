package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.UserRole;
import com.git.blog.dao.mapper.UserRoleMapper;
import com.git.blog.dao.service.UserRoleDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Service

@Slf4j
public class UserRoleDaoServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleDaoService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean isRole(Long uid, Long roleId) {
        if(uid==null || roleId==null) {
            return false;
        }
        int count = count(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUid,uid).eq(UserRole::getRoleId,roleId));
        return count>0;
    }
}
