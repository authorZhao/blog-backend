package com.git.blog.api.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.dto.blog.BlogArticleDetailVO;
import com.git.blog.dto.blog.BlogArticlePageDTO;
import com.git.blog.dto.blog.BlogArticleVO;
import com.git.blog.dto.blog.TagTypeDetailDTO;
import com.git.blog.service.ArticleService;
import com.git.blog.service.CacheService;
import com.git.blog.service.TagTypeService;
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
public class PageController {

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
        blogArticlePageDTO.setPageSize(2);
        blogArticlePageDTO.setCurrent(current);
        Page<BlogArticleVO> pages = articleService.pageArticle(blogArticlePageDTO);
        ModelAndView modelAndView = new ModelAndView("butterfly/index.html");
        modelAndView.addObject("pagesArticleList",pages);
        modelAndView.addObject("pagesArticleListTotalPage",pages.getPages());
        setTagType(modelAndView);
        return modelAndView;
    }

    @RequestMapping("/article/{id}")
    public ModelAndView articleId(@PathVariable("id")Long id){
        BlogArticleDetailVO blogArticleDetailVO = articleService.detailArticle(id);
        ModelAndView modelAndView = new ModelAndView("butterfly/blog.html");
        String content = blogArticleDetailVO.getContent();
        Map<String,String> parse = JSON.parseObject(content,Map.class);
        setTagType(modelAndView);
        modelAndView.addObject("article",blogArticleDetailVO);
        modelAndView.addObject("articleMap",parse);
        return modelAndView;
    }

    @RequestMapping("/tags/{id}")
    public ModelAndView tagId(@PathVariable("id")Long id){
        TagTypeDetailDTO tagDetail = tagTypeService.getTagDetail(id);
        ModelAndView modelAndView = new ModelAndView("butterfly/tag.html");
        setTagType(modelAndView);
        modelAndView.addObject("tagDetail",tagDetail);
        return modelAndView;
    }


    @RequestMapping("/categories/{id}")
    public ModelAndView categoriesId(@PathVariable("id")Long id){
        TagTypeDetailDTO typeDetail = tagTypeService.getTypeDetail(id);
        ModelAndView modelAndView = new ModelAndView("butterfly/categories.html");
        setTagType(modelAndView);
        modelAndView.addObject("typeDetail",typeDetail);
        return modelAndView;
    }

    @RequestMapping("/menu")
    public String menu(){
        return "web/menu/menu";
    }
    @RequestMapping("/role")
    public String role(){
        return "web/role/role";
    }
    @RequestMapping("/dept")
    public String dept(){
        return "web/dept/dept";
    }
    @RequestMapping("/article")
    public String article(){
        return "article/manage";
    }
    @RequestMapping("/dir/dir")
    public String dir(){
        return "web/file/dir";
    }



    private void setTagType(ModelAndView modelAndView){
        modelAndView.addObject("tags",cacheService.getTagType(CommonString.TAG));
        modelAndView.addObject("types",cacheService.getTagType(CommonString.TYPE));
        modelAndView.addObject("newArticles",articleService.getNewArticles(5));
    }
}
