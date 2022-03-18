package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.blog.commmon.CommonString;
import com.git.blog.dao.mapper.BlogArticleTagsMapper;
import com.git.blog.dto.blog.BlogTagDTO;
import com.git.blog.dto.model.entity.BlogArticleTags;
import com.git.blog.dto.model.entity.BlogArticleTypes;
import com.git.blog.dto.model.entity.BlogType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.BlogTag;
import com.git.blog.dao.mapper.BlogTagMapper;
import com.git.blog.dao.service.BlogTagDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签表，作为二级分类直接与uid关联 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Service
@Slf4j
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagDaoService {

    @Autowired
    private BlogTagMapper blogTagMapper;
    @Autowired
    private BlogArticleTagsMapper blogArticleTagsMapper;

    @Override
    public List<BlogTag> listByUid(Long uid) {
        if(uid==null)return Collections.emptyList();
        return list(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getCreateUid,uid));
    }


    public List<Long> listIdsByUid(Long uid) {
        if(uid==null)return Collections.emptyList();
        return list(new LambdaQueryWrapper<BlogTag>().select(BlogTag::getId)
                .eq(uid!=null,BlogTag::getCreateUid,uid)).stream().map(BlogTag::getId).collect(Collectors.toList());
    }


    @Override
    public Integer countByUid(Long uid) {
        if(uid==null)return 0;
        return count(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getCreateUid,uid));
    }


    @Override
    public List<Long> getTagIdsByArticleId(Long id) {
        if(id==null)return Collections.emptyList();
        List<BlogArticleTags> blogArticleTypes = blogArticleTagsMapper.selectList(new LambdaQueryWrapper<BlogArticleTags>()
                .select(BlogArticleTags::getTagId)
                .eq(BlogArticleTags::getArticleId, id));
        if(CollectionUtils.isEmpty(blogArticleTypes))return Collections.emptyList();
        List<Long> typeIds = blogArticleTypes.stream().map(BlogArticleTags::getTagId).distinct().collect(Collectors.toList());
        List<BlogTag> list = listByIds(typeIds);
        return list.stream().map(BlogTag::getId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<BlogTag> getTagsByArticleId(Long id) {
        if(id==null)return Collections.emptyList();
        List<BlogArticleTags> blogArticleTypes = blogArticleTagsMapper.selectList(new LambdaQueryWrapper<BlogArticleTags>()
                .select(BlogArticleTags::getTagId)
                .eq(BlogArticleTags::getArticleId, id));
        if(CollectionUtils.isEmpty(blogArticleTypes))return Collections.emptyList();
        List<Long> typeIds = blogArticleTypes.stream().map(BlogArticleTags::getTagId).distinct().collect(Collectors.toList());
        List<BlogTag> list = listByIds(typeIds);
        return list;
    }


    @Override
    public void deleteArticleTag(Long id) {
        if(id==null)return;
        BlogArticleTags blogArticleTypes = blogArticleTagsMapper.selectById(id);
        if(blogArticleTypes==null) return;
        blogArticleTagsMapper.deleteById(id);
    }

    @Override
    public List<Long> filterTagIds(List<Long> tagIds, Long uid) {
        if(CollectionUtils.isEmpty(tagIds))return Collections.emptyList();
        List<Long> typeIds = listIdsByUid(uid);
        if(CollectionUtils.isEmpty(typeIds))return Collections.emptyList();
        return tagIds.stream().filter(typeIds::contains).collect(Collectors.toList());
    }

    @Override
    public void saveByTagIdsAndArticleId(List<Long> tagIds, Long id) {
        tagIds.stream().distinct().forEach(i->{
            blogArticleTagsMapper.insert(new BlogArticleTags().setArticleId(id).setTagId(i));
        });
    }
}
