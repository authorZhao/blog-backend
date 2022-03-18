package com.git.blog.dao.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.BlogArticleTags;
import com.git.blog.dao.mapper.BlogArticleTagsMapper;
import com.git.blog.dao.service.BlogArticleTagsDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Service
public class BlogArticleTagsServiceImpl extends ServiceImpl<BlogArticleTagsMapper, BlogArticleTags> implements BlogArticleTagsDaoService {
    private final Logger logger = LoggerFactory.getLogger(BlogArticleTagsDaoService.class);

    @Autowired
    private BlogArticleTagsMapper blogArticleTagsMapper;
}
