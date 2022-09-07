package com.git.blog.dao.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.entity.ShiCiSongCi;
import com.git.blog.dao.mapper.ShiCiSongCiMapper;
import com.git.blog.dao.service.ShiCiSongCiService;
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
public class ShiCiSongCiDaoServiceImpl extends ServiceImpl<ShiCiSongCiMapper, ShiCiSongCi> implements ShiCiSongCiService {

    @Autowired
    private ShiCiSongCiMapper shiCiSongCiMapper;
}
