package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.dao.service.BlogArticleDaoService;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.blog.*;
import com.git.blog.dto.model.entity.BlogArticle;
import com.git.blog.dto.model.entity.BlogTag;
import com.git.blog.dto.model.entity.BlogType;
import com.git.blog.exception.BizException;
import com.git.blog.service.AuthService;
import com.git.blog.service.CacheService;
import com.git.blog.service.TagTypeService;
import com.git.blog.service.bean.BlogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2022-03-06
 */
@Service
@Slf4j
public class TagTypeServiceImpl implements TagTypeService{
    @Autowired
    private BlogTagDaoService blogTagDaoService;
    @Autowired
    private BlogTypeDaoService blogTypeDaoService;
    @Autowired
    private BlogArticleDaoService blogArticleDaoService;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private CacheService cacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addTag(BlogTagDTO blogTagDTO) {
        if(blogTagDTO==null || StringUtils.isEmpty(blogTagDTO.getName())){
            return Boolean.FALSE;
        }
        Integer integer = blogTagDaoService.countByUid(AuthTheadLocal.get());
        if(100<integer) throw new BizException("标签数量上限为100");

        blogTagDTO.setId(null);
        BlogTag blogTag = blogMapper.tagDTOToTag(blogTagDTO);
        return blogTagDaoService.save(blogTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTag(BlogTagDTO blogTagDTO) {
        if(blogTagDTO==null || blogTagDTO.getId()==null){
            return Boolean.FALSE;
        }

        BlogTag byId = blogTagDaoService.getById(blogTagDTO.getId());
        if(byId==null) throw new BizException("标签不存");
        boolean auth = authService.checkAuth(AuthTheadLocal.get(),CommonString.TAG_UPDATE_OTHER);
        if(BooleanUtils.isFalse(auth)) throw new BizException("没有权限修改标签");

        BlogTag blogTag = blogMapper.tagDTOToTag(blogTagDTO);
        blogTag.setCreateTime(null);
        blogTag.setUpdateTime(null);
        blogTag.setCreateUid(AuthTheadLocal.get());

        return blogTagDaoService.updateById(blogTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTag(Long id) {
        if(id==null){
            return Boolean.FALSE;
        }

        BlogTag byId = blogTagDaoService.getById(id);
        if(byId==null) throw new BizException("标签不存");
        boolean auth = authService.checkAuth(AuthTheadLocal.get(),CommonString.TAG_DELETE_OTHER);
        if(BooleanUtils.isFalse(auth)) throw new BizException("没有权限修改标签");

        return blogTagDaoService.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTags(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return Boolean.FALSE;
        }
        ids.forEach(this::deleteTag);
        return Boolean.TRUE;
    }

    @Override
    public List<BlogTagDTO> queryAllTags() {
        List<BlogTag> blogTagDTOList = blogTagDaoService.listByUid(AuthTheadLocal.get());
        return blogTagDTOList.stream().map(blogMapper::tagToTagDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addType(BlogTypeDTO blogTagDTO) {
        if(blogTagDTO==null || StringUtils.isEmpty(blogTagDTO.getName())){
            return Boolean.FALSE;
        }

        blogTagDTO.setId(null);
        blogTagDTO.setStatus(CommonString.TYPE_NORMAL_STATUS);
        BlogType blogTag = blogMapper.typeDTOToType(blogTagDTO);
        return blogTypeDaoService.save(blogTag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateType(BlogTypeDTO blogTagDTO) {
        if(blogTagDTO==null || blogTagDTO.getId()==null){
            return Boolean.FALSE;
        }

        BlogType byId1 = blogTypeDaoService.getById(blogTagDTO.getId());
        if(byId1==null) throw new BizException("文章类型不存在");
        BlogType blogType = new BlogType().setName(blogTagDTO.getName())
                .setDescription(blogTagDTO.getDescription())
                .setId(blogTagDTO.getId())
                .setPid(blogTagDTO.getPid())
                .setStatus(CommonString.TYPE_NORMAL_STATUS.equals(blogTagDTO.getStatus())?
                        CommonString.TYPE_NORMAL_STATUS:CommonString.TYPE_DELETE_STATUS)
                .setSort(blogTagDTO.getSort());


        return blogTypeDaoService.updateById(blogType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteType(Long id) {
        BlogType byId1 = blogTypeDaoService.getById(id);
        if(byId1==null) throw new BizException("文章类型不存在");

        BlogType blogType = new BlogType()
                .setId(id)
                .setStatus(CommonString.TYPE_DELETE_STATUS);
        return blogTypeDaoService.updateById(blogType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTypes(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return Boolean.FALSE;
        }
        ids.forEach(this::deleteType);
        return Boolean.TRUE;
    }

    @Override
    public List<BlogTypeDTO> queryAllTypes() {
        List<BlogType> blogTagDTOList = blogTypeDaoService.listByStatus(CommonString.TYPE_NORMAL_STATUS);
        return blogTagDTOList.stream().map(blogMapper::typeToTypesDTO).collect(Collectors.toList());
    }

    @Override
    public List<BlogTypeDTO> listTypes() {
        List<BlogType> blogTagDTOList = blogTypeDaoService.list();
        return blogTagDTOList.stream().map(blogMapper::typeToTypesDTO).collect(Collectors.toList());
    }

    @Override
    public List<BlogTypeDTO> treeTypes() {
        List<BlogTypeDTO> blogTypeDTOS = queryAllTypes();
        List<BlogTypeDTO>  treeList = genTree(blogTypeDTOS);
        return treeList;
    }

    @Override
    public TagTypeDetailDTO getTagDetail(Long tagId) {
        TagTypeDetailDTO tagTypeDetailDTO = new TagTypeDetailDTO();
        BlogTag tag = blogTagDaoService.getById(tagId);
        if(tag==null) {
            return tagTypeDetailDTO;
        }
        tagTypeDetailDTO.setId(tag.getId()).setName(tag.getName());

        List<Long> articleIds = blogTagDaoService.getArticleIds(tagId);
        if(CollectionUtils.isEmpty(articleIds)){
            return tagTypeDetailDTO;
        }
        List<BlogArticle> blogArticles = blogArticleDaoService.listByIdsAndStatus(articleIds,CommonString.ARTICLE_NORMAL_STATUS);
        if(CollectionUtils.isEmpty(blogArticles)){
            return tagTypeDetailDTO;
        }
        Map<Integer, List<BlogArticle>> map = blogArticles.stream().collect(Collectors.groupingBy(i -> i.getCreateTime().getYear()));

        List<BlogArticleYearDTO> blogArticleYearDTOList = new ArrayList<>();
        Comparator<BlogArticle> objectComparator = Comparator.comparingLong(i -> i.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        map.forEach((k,v)->{
            BlogArticleYearDTO blogArticleYearDTO = new BlogArticleYearDTO();
            blogArticleYearDTO.setYear(k);
            List<BlogArticleDTO> collect = v.stream().sorted(objectComparator.reversed()).map(blogMapper::articleToArticleDTO).collect(Collectors.toList());
            blogArticleYearDTO.setBlogArticleDTOList(collect);
            blogArticleYearDTOList.add(blogArticleYearDTO);
        });

        blogArticleYearDTOList.stream().sorted(Comparator.comparingInt(BlogArticleYearDTO::getYear)).collect(Collectors.toList());
        tagTypeDetailDTO.setBlogArticleYearDTOList(blogArticleYearDTOList);
        return tagTypeDetailDTO;
    }

    @Override
    public TagTypeDetailDTO getTypeDetail(Long typeId) {
        TagTypeDetailDTO tagTypeDetailDTO = new TagTypeDetailDTO();
        BlogType tag = blogTypeDaoService.getById(typeId);
        if(tag==null || !CommonString.TYPE_NORMAL_STATUS.equals(tag.getStatus())) {
            return tagTypeDetailDTO;
        }
        tagTypeDetailDTO.setId(tag.getId()).setName(tag.getName());

        List<Long> articleIds = blogTypeDaoService.getArticleIds(typeId);
        if(CollectionUtils.isEmpty(articleIds)){
            return tagTypeDetailDTO;
        }
        List<BlogArticle> blogArticles = blogArticleDaoService.listByIdsAndStatus(articleIds,CommonString.ARTICLE_NORMAL_STATUS);
        if(CollectionUtils.isEmpty(blogArticles)){
            return tagTypeDetailDTO;
        }
        Map<Integer, List<BlogArticle>> map = blogArticles.stream().collect(Collectors.groupingBy(i -> i.getCreateTime().getYear()));

        List<BlogArticleYearDTO> blogArticleYearDTOList = new ArrayList<>();
        Comparator<BlogArticle> objectComparator = Comparator.comparingLong(i -> i.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        map.forEach((k,v)->{
            BlogArticleYearDTO blogArticleYearDTO = new BlogArticleYearDTO();
            blogArticleYearDTO.setYear(k);
            List<BlogArticleDTO> collect = v.stream().sorted(objectComparator.reversed()).map(blogMapper::articleToArticleDTO).collect(Collectors.toList());
            blogArticleYearDTO.setBlogArticleDTOList(collect);
            blogArticleYearDTOList.add(blogArticleYearDTO);
        });

        blogArticleYearDTOList.stream().sorted(Comparator.comparingInt(BlogArticleYearDTO::getYear)).collect(Collectors.toList());
        tagTypeDetailDTO.setBlogArticleYearDTOList(blogArticleYearDTOList);
        return tagTypeDetailDTO;
    }

    @Override
    public List<TagTypeCountDTO> getTagType(String key) {
        if(StringUtils.isEmpty(key)){
            return Collections.emptyList();
        }
        List<TagTypeCountDTO> tagTypeCountDTOS = Collections.emptyList();
        String str = cacheService.getStr(key);

        //走缓存
        if (StringUtils.isNotEmpty(str)) {
            return JSON.parseArray(str, TagTypeCountDTO.class);
        }

        //查数据
        if (CommonString.TAG.equals(key)) {
            tagTypeCountDTOS = blogTagDaoService.getTagsCount();
            cacheService.setStr(CommonString.TAG, JSON.toJSONString(tagTypeCountDTOS));
            return tagTypeCountDTOS;
        }

        tagTypeCountDTOS = blogTypeDaoService.getTypesCount();
        cacheService.setStr(CommonString.TYPE, JSON.toJSONString(tagTypeCountDTOS));
        return tagTypeCountDTOS;
    }

    private List<BlogTypeDTO> genTree(List<BlogTypeDTO> blogTypeDTOS) {
        return blogTypeDTOS;
    }
}
