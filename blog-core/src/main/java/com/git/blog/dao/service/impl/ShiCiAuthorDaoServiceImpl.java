package com.git.blog.dao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.entity.ShiCiAuthor;
import com.git.blog.dao.mapper.ShiCiAuthorMapper;
import com.git.blog.dao.service.ShiCiAuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2022-09-05
 */
@Service
public class ShiCiAuthorDaoServiceImpl extends ServiceImpl<ShiCiAuthorMapper, ShiCiAuthor> implements ShiCiAuthorService {

    @Autowired
    private ShiCiAuthorMapper shiCiAuthorMapper;
}
