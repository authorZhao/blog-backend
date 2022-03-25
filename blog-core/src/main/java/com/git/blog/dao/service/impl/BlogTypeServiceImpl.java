package com.git.blog.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.git.blog.commmon.CommonString;
import com.git.blog.dao.mapper.BlogArticleTypesMapper;
import com.git.blog.dto.blog.BlogArticleTypesDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;
import com.git.blog.dto.model.entity.BlogArticleTags;
import com.git.blog.dto.model.entity.BlogArticleTypes;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.git.blog.dto.model.entity.BlogType;
import com.git.blog.dao.mapper.BlogTypeMapper;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章分类表 服务实现类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Service
public class BlogTypeServiceImpl extends ServiceImpl<BlogTypeMapper, BlogType> implements BlogTypeDaoService {
    private final Logger logger = LoggerFactory.getLogger(BlogTypeDaoService.class);

    @Autowired
    private BlogTypeMapper blogTypeMapper;
    @Autowired
    private BlogArticleTypesMapper blogArticleTypesMapper;

    @Override
    public List<BlogType> listByStatus(Integer status) {
        if(status==null) return Collections.emptyList();
        return list(new LambdaQueryWrapper<BlogType>().eq(BlogType::getStatus,status));
    }

    public List<Long> listIdsByStatus(Integer status) {
        if(status==null) return Collections.emptyList();
        return list(new LambdaQueryWrapper<BlogType>().select(BlogType::getId).eq(BlogType::getStatus,status))
                .stream().map(BlogType::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getTypeIdsByArticleId(Long id) {
        if(id==null)return Collections.emptyList();
        List<BlogArticleTypes> blogArticleTypes = blogArticleTypesMapper.selectList(new LambdaQueryWrapper<BlogArticleTypes>()
                .select(BlogArticleTypes::getTypeId)
                .eq(BlogArticleTypes::getArticleId, id));
        if(CollectionUtils.isEmpty(blogArticleTypes))return Collections.emptyList();
        List<Long> typeIds = blogArticleTypes.stream().map(BlogArticleTypes::getTypeId).distinct().collect(Collectors.toList());

        List<BlogType> list = list(new LambdaQueryWrapper<BlogType>().in(BlogType::getId, typeIds).eq(BlogType::getStatus, CommonString.TYPE_NORMAL_STATUS));
        return list.stream().map(BlogType::getId).distinct().collect(Collectors.toList());
    }

    @Override
    public List<BlogType> getTypesByArticleId(Long id) {
        if(id==null)return Collections.emptyList();
        List<BlogArticleTypes> blogArticleTypes = blogArticleTypesMapper.selectList(new LambdaQueryWrapper<BlogArticleTypes>()
                .select(BlogArticleTypes::getTypeId)
                .eq(BlogArticleTypes::getArticleId, id));
        if(CollectionUtils.isEmpty(blogArticleTypes))return Collections.emptyList();
        List<Long> typeIds = blogArticleTypes.stream().map(BlogArticleTypes::getTypeId).distinct().collect(Collectors.toList());
        List<BlogType> list = list(new LambdaQueryWrapper<BlogType>().in(BlogType::getId, typeIds).eq(BlogType::getStatus, CommonString.TYPE_NORMAL_STATUS));
        return list;
    }

    @Override
    public List<BlogArticleTypesDTO> getTypesByArticleIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids))return Collections.emptyList();
        List<BlogArticleTypesDTO> blogArticleTypesDTOList = blogArticleTypesMapper.getTypesByArticleIds(ids);
        return blogArticleTypesDTOList;
    }

    @Override
    public void deleteArticleType(Long typeId) {
        if(typeId==null)return;
        BlogArticleTypes blogArticleTypes = blogArticleTypesMapper.selectById(typeId);
        if(blogArticleTypes==null) return;
        blogArticleTypesMapper.deleteById(blogArticleTypes.getTypeId());
    }

    @Override
    public List<Long> filterTypeIds(List<Long> tagIds) {
        if(CollectionUtils.isEmpty(tagIds))return Collections.emptyList();
        List<Long> typeIds = listIdsByStatus(CommonString.TYPE_NORMAL_STATUS);
        if(CollectionUtils.isEmpty(typeIds))return Collections.emptyList();
        return tagIds.stream().filter(typeIds::contains).collect(Collectors.toList());
    }

    @Override
    public void saveByTypeIdsAndArticleId(List<Long> typeIds, Long id) {
        typeIds.stream().distinct().forEach(i->{
            blogArticleTypesMapper.insert(new BlogArticleTypes().setArticleId(id).setTypeId(i));
        });
    }

    @Override
    public List<TagTypeCountDTO> getTypesCount() {
        return blogArticleTypesMapper.getTypesCount();
    }

    @Override
    public List<Long> getArticleIds(Long typeId) {
        if(typeId==null){
            return Collections.emptyList();
        }
        return blogArticleTypesMapper.selectList(new LambdaQueryWrapper<BlogArticleTypes>().select(BlogArticleTypes::getArticleId)
                .eq(BlogArticleTypes::getTypeId,typeId)).stream().map(BlogArticleTypes::getArticleId).distinct().collect(Collectors.toList());
    }
}
