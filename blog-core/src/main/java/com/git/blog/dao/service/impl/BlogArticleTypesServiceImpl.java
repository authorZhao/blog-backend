package com.git.blog.dao.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.BlogArticleTypes;
import com.git.blog.dao.mapper.BlogArticleTypesMapper;
import com.git.blog.dao.service.BlogArticleTypesDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章类型表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Service
public class BlogArticleTypesServiceImpl extends ServiceImpl<BlogArticleTypesMapper, BlogArticleTypes> implements BlogArticleTypesDaoService {
    private final Logger logger = LoggerFactory.getLogger(BlogArticleTypesDaoService.class);

    @Autowired
    private BlogArticleTypesMapper blogArticleTypesMapper;
}
