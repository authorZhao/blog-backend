package com.git.blog.dao.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.Role;
import com.git.blog.dao.mapper.RoleMapper;
import com.git.blog.dao.service.RoleDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Service

@Slf4j
public class RoleDaoServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleDaoService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public Role getByName(String roleName) {
        List<Role> list = list(new LambdaQueryWrapper<Role>().eq(StringUtils.isNotBlank(roleName), Role::getRoleName, roleName));

        if(CollectionUtils.isEmpty(list)){
            return null;
        }

        if(list.size()>1){
            log.error("角色名重复list={}", JSON.toJSONString(list));
        }

        return list.get(0);
    }
}
