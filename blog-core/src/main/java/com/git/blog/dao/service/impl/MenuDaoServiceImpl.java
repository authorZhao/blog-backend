package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.Menu;
import com.git.blog.dao.mapper.MenuMapper;
import com.git.blog.dao.service.MenuDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@Service
@Slf4j
public class MenuDaoServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuDaoService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Menu getByName(String menuName) {
        List<Menu> list = list(new LambdaQueryWrapper<Menu>().eq(Menu::getMenuName, menuName));

        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
