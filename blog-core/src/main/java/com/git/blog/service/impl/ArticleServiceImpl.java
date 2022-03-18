package com.git.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.dao.service.BlogArticleDaoService;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.blog.BlogArticleDTO;
import com.git.blog.dto.blog.BlogArticleDetailVO;
import com.git.blog.dto.blog.BlogArticlePageDTO;
import com.git.blog.dto.blog.BlogArticleVO;
import com.git.blog.dto.model.entity.BlogArticle;
import com.git.blog.dto.model.entity.User;
import com.git.blog.exception.BizException;
import com.git.blog.service.ArticleService;
import com.git.blog.service.AuthService;
import com.git.blog.service.HexoService;
import com.git.blog.service.UserService;
import com.git.blog.service.bean.BlogMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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


    @Override
    public Page pageArticle(BlogArticlePageDTO blogArticlePageDTO) {
        Long uid = AuthTheadLocal.get();
        Wrapper<BlogArticle> wrapper = new LambdaQueryWrapper<BlogArticle>()
                .eq(!authService.checkAuth(uid, CommonString.ARTICLE_READ_LIST_OTHER), BlogArticle::getUserId, uid)
                .eq(!authService.checkAuth(uid, CommonString.ARTICLE_READ_DELETE), BlogArticle::getStatus, CommonString.ARTICLE_NORMAL_STATUS);
        Page<BlogArticle> page = new Page<>(blogArticlePageDTO.getCurrent(), blogArticlePageDTO.getPageSize());
        page = blogArticleDaoService.page(page, wrapper);

        if(CollectionUtils.isEmpty(page.getRecords())){
            return page;
        }

        List<Long> uidList = page.getRecords().stream().map(BlogArticle::getUserId).collect(Collectors.toList());
        Map<Long, User> userMapByUid = userService.getUserMapByUid(uidList);

        List collect = page.getRecords().stream().map(i->{
            BlogArticleVO blogArticleVO = blogMapper.articleToArticleVO(i);
            blogArticleVO.setAuthor(Optional.ofNullable(userMapByUid.get(i.getUserId())).map(User::getNickname).orElse(StringUtils.EMPTY));
            return blogArticleVO;
        }).collect(Collectors.toList());


        page.setRecords(collect);
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

        List<Long> tagIds = blogTagDaoService.getTagIdsByArticleId(id);
        List<Long> typeIds = blogTypeDaoService.getTypeIdsByArticleId(id);
        blogArticleDetailVO.setTagIds(tagIds);
        blogArticleDetailVO.setTypeIds(typeIds);
        return blogArticleDetailVO;
    }

    @Override
    public Boolean init() {
        //1.删除全部md
        hexoService.deleteAllMd();

        //2.生成md
        List<BlogArticle> list = blogArticleDaoService.list();
        if(CollectionUtils.isEmpty(list))return Boolean.TRUE;
        list.forEach(i->{
            if(!CommonString.ARTICLE_NORMAL_STATUS.equals(i.getStatus()))return;
            List<Long> tagIds = blogTagDaoService.getTagIdsByArticleId(i.getId());
            List<Long> typeIds = blogTypeDaoService.getTypeIdsByArticleId(i.getId());
            hexoService.generateMd(i,typeIds,tagIds,AuthTheadLocal.get());
        });

        //3.执行命令
        hexoService.exeHexoCleanGenerate();
        return Boolean.TRUE;
    }
}
