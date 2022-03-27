package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.PageParam;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.dao.service.BlogArticleDaoService;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.blog.*;
import com.git.blog.dto.model.entity.BlogArticle;
import com.git.blog.dto.model.entity.BlogTag;
import com.git.blog.dto.model.entity.BlogType;
import com.git.blog.dto.model.entity.User;
import com.git.blog.exception.BizException;
import com.git.blog.service.*;
import com.git.blog.service.bean.BlogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2022-03-06
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private BlogArticleDaoService blogArticleDaoService;
    @Autowired
    private AuthService authService;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogTagDaoService blogTagDaoService;
    @Autowired
    private BlogTypeDaoService blogTypeDaoService;
    @Autowired
    private UserService userService;
    @Autowired
    private HexoService hexoService;
    @Autowired
    private CacheService cacheService;



    @Override
    public Page pageArticle(BlogArticlePageDTO blogArticlePageDTO) {
        Long uid = AuthTheadLocal.get();
        Wrapper<BlogArticle> wrapper = new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getId,BlogArticle::getTitle,BlogArticle::getUserId,BlogArticle::getCoverImage,BlogArticle::getEditorType
                        ,BlogArticle::getQrcodePath,BlogArticle::getTop,BlogArticle::getStatus,BlogArticle::getRecommended
                        ,BlogArticle::getOriginal,BlogArticle::getDescription,BlogArticle::getKeywords,BlogArticle::getComment
                        ,BlogArticle::getPassword,BlogArticle::getRequiredAuth,BlogArticle::getReprintUrl,BlogArticle::getCreateTime,BlogArticle::getUpdateTime)
                .eq(!authService.checkAuth(uid, CommonString.ARTICLE_READ_LIST_OTHER), BlogArticle::getUserId, uid)
                .eq(!authService.checkAuth(uid, CommonString.ARTICLE_READ_DELETE), BlogArticle::getStatus, CommonString.ARTICLE_NORMAL_STATUS);
        Page<BlogArticle> page = new Page<>(blogArticlePageDTO.getCurrent(), blogArticlePageDTO.getPageSize());
        page = blogArticleDaoService.page(page, wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())){
            return page;
        }

        List<Long> uidList = page.getRecords().stream().map(BlogArticle::getUserId).collect(Collectors.toList());
        List<Long> articleIds = page.getRecords().stream().map(BlogArticle::getId).collect(Collectors.toList());

        Map<Long, User> userMapByUid = userService.getUserMapByUid(uidList);

        List<BlogArticleTypesDTO> typesByArticleIds = blogTypeDaoService.getTypesByArticleIds(articleIds);
        Map<Long, List<BlogArticleTypesDTO>> typeArticleMap = typesByArticleIds.stream().collect(Collectors.groupingBy(BlogArticleTypesDTO::getArticleId));

        List<BlogArticleTagsDTO> blogArticleTypesDTOList = blogTagDaoService.getTagsByArticleIds(articleIds);
        Map<Long, List<BlogArticleTagsDTO>> tagArticleMap = blogArticleTypesDTOList.stream().collect(Collectors.groupingBy(BlogArticleTagsDTO::getArticleId));


        List collect = page.getRecords().stream().map(i->{
            BlogArticleVO blogArticleVO = blogMapper.articleToArticleVO(i);
            blogArticleVO.setTagIds(blogArticleTypesDTOList.stream().map(BlogArticleTagsDTO::getId).distinct().collect(Collectors.toList()));
            blogArticleVO.setTypeIds(typesByArticleIds.stream().map(BlogArticleTypesDTO::getId).distinct().collect(Collectors.toList()));
            blogArticleVO.setTags(Optional.ofNullable(tagArticleMap.get(i.getId())).orElse(Collections.emptyList()).stream().map(BlogArticleTagsDTO::getName).filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")));
            blogArticleVO.setTypes(Optional.ofNullable(typeArticleMap.get(i.getId())).orElse(Collections.emptyList()).stream().map(BlogArticleTypesDTO::getName).filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")));
            blogArticleVO.setAuthor(Optional.ofNullable(userMapByUid.get(i.getUserId())).map(User::getNickname).orElse(StringUtils.EMPTY));
            return blogArticleVO;
        }).collect(Collectors.toList());

        page.setRecords(collect);
        return page;
    }

    @Override
    public Page pageArchives(PageParam param) {
        Wrapper<BlogArticle> wrapper = new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getId,BlogArticle::getTitle,BlogArticle::getUserId,BlogArticle::getCoverImage,BlogArticle::getEditorType
                        ,BlogArticle::getQrcodePath,BlogArticle::getTop,BlogArticle::getStatus,BlogArticle::getRecommended
                        ,BlogArticle::getOriginal,BlogArticle::getDescription,BlogArticle::getKeywords,BlogArticle::getComment
                        ,BlogArticle::getPassword,BlogArticle::getRequiredAuth,BlogArticle::getReprintUrl,BlogArticle::getCreateTime,BlogArticle::getUpdateTime)
                .orderByDesc(BlogArticle::getCreateTime);
        Page<BlogArticle> page = new Page<>(param.getCurrent(), param.getPageSize());
        page = blogArticleDaoService.page(page, wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())){
            return page;
        }
        List<BlogArticle> records = page.getRecords();

        Map<Integer, List<BlogArticle>> map = records.stream().collect(Collectors.groupingBy(i -> i.getCreateTime().getYear()));

        List blogArticleYearDTOList = new ArrayList<>();
        Comparator<BlogArticle> objectComparator = Comparator.comparingLong(i -> i.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        map.forEach((k,v)->{
            BlogArticleYearDTO blogArticleYearDTO = new BlogArticleYearDTO();
            blogArticleYearDTO.setYear(k);
            List<BlogArticleDTO> collect = v.stream().sorted(objectComparator.reversed()).map(blogMapper::articleToArticleDTO).collect(Collectors.toList());
            blogArticleYearDTO.setBlogArticleDTOList(collect);
            blogArticleYearDTOList.add(blogArticleYearDTO);
        });

        blogArticleYearDTOList.stream().sorted(Comparator.comparingInt(BlogArticleYearDTO::getYear)).collect(Collectors.toList());
        page.setRecords(blogArticleYearDTOList);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdate(BlogArticleDTO blogArticlePageDTO) {
        //1.简单粗暴，删除新增 校验
        Long id = blogArticlePageDTO.getId();
            check(blogArticlePageDTO);
        List<Long> tagIds = blogArticlePageDTO.getTagIds();
        List<Long> typeIds = blogArticlePageDTO.getTypeIds();

        //2.删除
        if(id!=null){
            //realDeleteArticle(id);
            blogTagDaoService.deleteArticleTag(id);
            blogTypeDaoService.deleteArticleType(id);

        }
        tagIds = blogTagDaoService.filterTagIds(tagIds,AuthTheadLocal.get());
        typeIds = blogTypeDaoService.filterTypeIds(typeIds);

        //3.新增
        BlogArticle blogArticle = blogMapper.articleDTOToArticle(blogArticlePageDTO);

        if(id!=null){
            //realDeleteArticle(id);
            blogArticle.setCreateTime(null);
            blogArticle.setUpdateTime(LocalDateTime.now());
            blogArticleDaoService.updateById(blogArticle);
        }else{
            blogArticleDaoService.save(blogArticle);
        }


        blogTagDaoService.saveByTagIdsAndArticleId(tagIds,blogArticle.getId());
        blogTypeDaoService.saveByTypeIdsAndArticleId(typeIds,blogArticle.getId());

        hexoService.generateHtml(blogArticle,typeIds,tagIds,AuthTheadLocal.get());

        return Boolean.TRUE;
    }

    private void check(BlogArticleDTO blogArticlePageDTO) {

    }

    private void realDeleteArticle(Long id) {
        BlogArticle byId = blogArticleDaoService.getById(id);
        if(byId==null)return;
        if(Objects.equals(byId.getUserId(),AuthTheadLocal.get())){
            blogArticleDaoService.removeById(id);
        }
        boolean b = authService.checkAuth(AuthTheadLocal.get(), CommonString.ARTICLE_DELETE_OTHER);
        if(b){
            blogArticleDaoService.removeById(id);
            return;
        }
        throw new BizException("无权限删除别人的文章");
    }

    @Override
    public Boolean deleteArticle(Long id) {
        BlogArticle byId = blogArticleDaoService.getById(id);
        if(byId==null) return Boolean.FALSE;
        BlogArticle blogArticle = new BlogArticle().setId(byId.getId()).setStatus(CommonString.ARTICLE_DELETE_STATUS);
        return blogArticleDaoService.updateById(blogArticle);
    }

    @Override
    public Boolean recoveryArticle(Long id) {
        BlogArticle byId = blogArticleDaoService.getById(id);
        if(byId==null) return Boolean.FALSE;
        BlogArticle blogArticle = new BlogArticle().setId(byId.getId()).setStatus(CommonString.ARTICLE_NORMAL_STATUS);
        return blogArticleDaoService.updateById(blogArticle);
    }

    @Override
    public BlogArticleDetailVO detailArticle(Long id) {
        BlogArticle byId = blogArticleDaoService.getById(id);
        if(byId==null) return null;
        BlogArticleDetailVO blogArticleDetailVO = blogMapper.articleToArticleDetailVO(byId);

        List<BlogTag> blogTagList = blogTagDaoService.getTagsByArticleId(id);
        List<BlogType> blogTypeList = blogTypeDaoService.getTypesByArticleId(id);
        blogArticleDetailVO.setTagIds(blogTagList.stream().map(BlogTag::getId).distinct().collect(Collectors.toList()));
        blogArticleDetailVO.setTypeIds(blogTypeList.stream().map(BlogType::getId).distinct().collect(Collectors.toList()));
        blogArticleDetailVO.setBlogTypeDTOList(blogTypeList.stream().map(blogMapper::typeToTypesDTO).collect(Collectors.toList()));
        blogArticleDetailVO.setBlogTagDTOList(blogTagList.stream().map(blogMapper::tagToTagDTO).collect(Collectors.toList()));
        return blogArticleDetailVO;
    }

    @Override
    public Boolean init() {
        //1.删除全部md
        hexoService.deleteAllMd();

        //2.生成md
        List<BlogArticle> list = blogArticleDaoService.list();
        if(CollectionUtils.isEmpty(list))return Boolean.TRUE;
        Map<BlogArticle,File> map = new HashMap<>();
        list.forEach(i->{
            if(!CommonString.ARTICLE_NORMAL_STATUS.equals(i.getStatus()))return;
            List<Long> tagIds = blogTagDaoService.getTagIdsByArticleId(i.getId());
            List<Long> typeIds = blogTypeDaoService.getTypeIdsByArticleId(i.getId());
            File file = hexoService.generateMd(i, typeIds, tagIds, AuthTheadLocal.get());
            map.put(i,file);
        });

        //3.执行命令
        hexoService.exeHexoCleanGenerate();

        //解析html
        map.forEach((k,v)->{
            hexoService.parseHtml(k,v);
        });
        return Boolean.TRUE;
    }

    @Override
    public List<BlogArticleDTO> getNewArticles(Integer limit) {
        String str = cacheService.getStr(CommonString.ARTICLE_NEW+limit);
        if(StringUtils.isNotEmpty(str)){
            return JSON.parseArray(str,BlogArticleDTO.class);
        }
        List<BlogArticleDTO> collect = blogArticleDaoService.listLimitAndStatus(limit, CommonString.ARTICLE_NORMAL_STATUS)
                .stream().map(blogMapper::articleToArticleDTO).collect(Collectors.toList());
        cacheService.setStr(CommonString.ARTICLE_NEW+limit,JSON.toJSONString(collect),30L, TimeUnit.MINUTES);
        return collect;
    }
}
