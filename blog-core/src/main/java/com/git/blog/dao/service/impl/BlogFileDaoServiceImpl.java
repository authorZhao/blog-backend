package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.git.blog.commmon.CommonString;
import com.git.blog.dao.mapper.BlogFileMapper;
import com.git.blog.dao.service.BlogFileDaoService;
import com.git.blog.dto.model.entity.BlogFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author authorZhao
 * @since 2022-03-03
 */
@Service
@Slf4j
public class BlogFileDaoServiceImpl extends ServiceImpl<BlogFileMapper, BlogFile> implements BlogFileDaoService {

    @Override
    public BlogFile getByHashId(String hashId) {
        if(hashId==null)return null;
        return baseMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                .eq(BlogFile::getHashId,hashId)
                .last(CommonString.LAST_SQL_LIMIT_1)
        );
    }
}
