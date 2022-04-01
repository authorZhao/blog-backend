package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.blog.commmon.CommonString;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.BlogArticle;
import com.git.blog.dao.mapper.BlogArticleMapper;
import com.git.blog.dao.service.BlogArticleDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements BlogArticleDaoService {
    private final Logger logger = LoggerFactory.getLogger(BlogArticleDaoService.class);

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Override
    public BlogArticle getPreOrNext(Long id, Boolean pre) {
        if(id==null){
            return null;
        }
        return blogArticleMapper.selectOne(new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getId,BlogArticle::getTitle,BlogArticle::getCoverImage)
                .lt(BooleanUtils.isTrue(pre),BlogArticle::getId,id)
                .gt(BooleanUtils.isFalse(pre),BlogArticle::getId,id)
                .eq(BlogArticle::getStatus,CommonString.ARTICLE_NORMAL_STATUS)
                .orderByDesc(BooleanUtils.isTrue(pre),BlogArticle::getId)
                .last(CommonString.LAST_SQL_LIMIT_1)
        );
    }
}
