package com.git.blog.dao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.entity.ShiCiType;
import com.git.blog.dao.mapper.ShiCiTypeMapper;
import com.git.blog.dao.service.ShiCiTypeDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2022-09-08
 */
@Service
public class ShiCiTypeDaoServiceImpl extends ServiceImpl<ShiCiTypeMapper, ShiCiType> implements ShiCiTypeDaoService {

    @Autowired
    private ShiCiTypeMapper shiCiTypeMapper;
}
