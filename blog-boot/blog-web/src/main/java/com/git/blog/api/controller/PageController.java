package com.git.blog.api.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.PageParam;
import com.git.blog.config.properties.BlogProperties;
import com.git.blog.dto.blog.*;
import com.git.blog.service.ArticleService;
import com.git.blog.service.CacheService;
import com.git.blog.service.TagTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 页面跳转的
 */
@Controller
@Slf4j
public class PageController {

    @Autowired
    private BlogProperties blogProperties;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private TagTypeService tagTypeService;

    @RequestMapping("/")
    public ModelAndView goToIndex(){
        return goToIndex(1);
    }

    @RequestMapping("/index/{current}")
    public ModelAndView goToIndex(@PathVariable("current") Integer current){
        BlogArticlePageDTO blogArticlePageDTO = new BlogArticlePageDTO();
        blogArticlePageDTO.setPageSize(10);
        blogArticlePageDTO.setCurrent(current);
        Page<BlogArticleVO> pages = articleService.pageArticle(blogArticlePageDTO);
        ModelAndView modelAndView = new ModelAndView("butterfly/index.html");
        modelAndView.addObject("page",pages);
        modelAndView.addObject("totalPage",pages.getPages());
        modelAndView.addObject("hrefPath","/index");
        BlogProperties clone = blogProperties.clone();
        clone.getConfigSite().put("isHome",true);
        setTagType(modelAndView,clone);
        return modelAndView;
    }

    @RequestMapping("/article/{id}")
    public ModelAndView articleId(@PathVariable("id")Long id){
        BlogArticleDetailVO blogArticleDetailVO = articleService.detailArticle(id);
        ModelAndView modelAndView = new ModelAndView("butterfly/blog.html");
        String content = blogArticleDetailVO.getContent();
        modelAndView.addObject("article",blogArticleDetailVO);
        BlogProperties clone = blogProperties.clone();
        clone.getConfigSite().put("isPost",true);
        blogArticleDetailVO.setContentMd("'"+blogArticleDetailVO.getContentMd()+"'");
        try {
            HtmlContent parse = JSON.parseObject(content, HtmlContent.class);
            if(parse!=null && StringUtils.isNotEmpty(parse.getTocHtml())){
                clone.getConfigSite().put("isToc",true);
            }
            modelAndView.addObject("articleMap", parse);
        }catch (Exception e){
            clone.getConfigSite().put("isToc",false);
        }
        clone.getConfigSite().put("title",blogArticleDetailVO.getTitle()+" | "+blogProperties.getTitle());
        clone.setTitle(blogArticleDetailVO.getTitle());
        clone.setOgTitle(blogArticleDetailVO.getTitle());
        setTagType(modelAndView,clone);
        return modelAndView;
    }

    @RequestMapping("/tags/{id}")
    public ModelAndView tagId(@PathVariable("id")Long id){
        TagTypeDetailDTO tagDetail = tagTypeService.getTagDetail(id);
        ModelAndView modelAndView = new ModelAndView("butterfly/tag.html");
        modelAndView.addObject("tagDetail",tagDetail);

        BlogProperties clone = blogProperties.clone();
        clone.getConfigSite().put("title",tagDetail.getName());
        clone.setTitle(tagDetail.getName());
        clone.setOgTitle(tagDetail.getName());
        setTagType(modelAndView,clone);
        return modelAndView;
    }


    @RequestMapping("/categories/{id}")
    public ModelAndView categoriesId(@PathVariable("id")Long id){
        TagTypeDetailDTO typeDetail = tagTypeService.getTypeDetail(id);
        ModelAndView modelAndView = new ModelAndView("butterfly/categories.html");
        modelAndView.addObject("typeDetail",typeDetail);

        BlogProperties clone = blogProperties.clone();
        clone.getConfigSite().put("title",typeDetail.getName());
        clone.setTitle(typeDetail.getName());
        clone.setOgTitle(typeDetail.getName());
        setTagType(modelAndView,clone);
        return modelAndView;
    }

    @RequestMapping("/archives")
    public ModelAndView archives(){
        return archivesCurrent(1);
    }

    @RequestMapping("/archives/{current}")
    public ModelAndView archivesCurrent(@PathVariable("current")Integer current){
        PageParam pageParam = new PageParam();
        pageParam.setCurrent(current);
        pageParam.setPageSize(1);
        Page<List<BlogArticleYearDTO>> page = articleService.pageArchives(pageParam);
        ModelAndView modelAndView = new ModelAndView("butterfly/archives.html");
        modelAndView.addObject("page",page);
        modelAndView.addObject("totalPage",page.getPages());
        modelAndView.addObject("hrefPath","/archives");

        BlogProperties clone = blogProperties.clone();
        clone.setTitle("归档");
        clone.setOgTitle("归档");
        clone.getConfigSite().put("title","归档");
        setTagType(modelAndView);
        return modelAndView;
    }



    private void setTagType(ModelAndView modelAndView,BlogProperties blogPropertiesParam){
        modelAndView.addObject("head",blogPropertiesParam);
        modelAndView.addObject("tags",cacheService.getTagType(CommonString.TAG));
        modelAndView.addObject("types",cacheService.getTagType(CommonString.TYPE));
        modelAndView.addObject("newArticles",articleService.getNewArticles(5));
    }

    private void setTagType(ModelAndView modelAndView){
        modelAndView.addObject("head",blogProperties);
        modelAndView.addObject("tags",cacheService.getTagType(CommonString.TAG));
        modelAndView.addObject("types",cacheService.getTagType(CommonString.TYPE));
        modelAndView.addObject("newArticles",articleService.getNewArticles(5));
    }
}
