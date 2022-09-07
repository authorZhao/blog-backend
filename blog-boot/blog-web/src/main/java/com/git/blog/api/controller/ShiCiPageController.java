package com.git.blog.api.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.PageParam;
import com.git.blog.config.properties.BlogProperties;
import com.git.blog.config.properties.ShiCiProperties;
import com.git.blog.dao.service.ShiCiTypeDaoService;
import com.git.blog.dto.blog.*;
import com.git.blog.dto.shici.ShiCiSongCiDTO;
import com.git.blog.entity.ShiCiSongCi;
import com.git.blog.entity.ShiCiType;
import com.git.blog.service.ArticleService;
import com.git.blog.service.ShiCiService;
import com.git.blog.service.TagTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 页面跳转的
 */
@Controller
@Slf4j
public class ShiCiPageController {

    @Autowired
    private BlogProperties blogProperties;
    @Autowired
    private ShiCiProperties shiCiProperties;
    @Autowired
    private ShiCiService shiCiService;


    @RequestMapping("/shici")
    public ModelAndView index(){
        return goIndex();
    }

    @RequestMapping("/shici/index")
    public ModelAndView goIndex(){
        ModelAndView modelAndView = new ModelAndView("shici/templates/index.html");
        setNormal(modelAndView);
        List<ShiCiType> list = shiCiService.listType();
        modelAndView.addObject("type",list);
        return modelAndView;
    }



    @RequestMapping("/shici/page/type/{type}/{current}")
    public ModelAndView pageType(@PathVariable("type") Long type,@PathVariable("current") Integer current){
        PageParam pageParam = new PageParam();
        pageParam.setCurrent(current);
        pageParam.setPageSize(shiCiProperties.getPageSize());
        Page<ShiCiSongCiDTO> page = shiCiService.pageByType(type,pageParam);
        ModelAndView modelAndView = new ModelAndView("shici/templates/pagetype.html");
        modelAndView.addObject("page",page);
        modelAndView.addObject("totalPage",page.getPages());
        modelAndView.addObject("hrefPath","/shici/page/type/"+type);
        modelAndView.addObject("typeName", Optional.ofNullable(shiCiService.getTypeById(type)).map(ShiCiType::getTypeName).orElse("诗词"));

        setNormal(modelAndView);

        return modelAndView;
    }

    @RequestMapping("/shici/detail/{id}")
    public ModelAndView getById(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("shici/templates/detail.html");
        ShiCiSongCiDTO shiCiSongCiDTO = shiCiService.getShiCiById(id);
        modelAndView.addObject("shiCi",shiCiSongCiDTO);
        setNormal(modelAndView);
        return modelAndView;
    }

    @RequestMapping("/shici/page/author/{author}/{current}")
    public ModelAndView pageAuthor(@PathVariable("author") String author,@PathVariable("current") Integer current){
        ModelAndView modelAndView = new ModelAndView("shici/templates/index.html");
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> map3 = new HashMap<>();
        map.put("author",blogProperties.getAuthor());
        map.put("shici",map3);
        map3.put("description",shiCiProperties.getDescription());
        map3.put("keyword",shiCiProperties.getKeyword());
        modelAndView.addObject("head",map);
        List<ShiCiType> list = shiCiService.listType();
        modelAndView.addObject("type",list);
        return modelAndView;
    }




    /*private void setTagType(ModelAndView modelAndView,BlogProperties blogPropertiesParam){
        modelAndView.addObject("head",blogPropertiesParam);
        modelAndView.addObject("tags",tagTypeService.getTagType(CommonString.TAG));
        modelAndView.addObject("types",tagTypeService.getTagType(CommonString.TYPE));
        modelAndView.addObject("newArticles",articleService.getNewArticles(5));
    }*/

    private void setTagType(ModelAndView modelAndView){
        /*modelAndView.addObject("head",blogProperties);
        modelAndView.addObject("tags",tagTypeService.getTagType(CommonString.TAG));
        modelAndView.addObject("types",tagTypeService.getTagType(CommonString.TYPE));
        modelAndView.addObject("newArticles",articleService.getNewArticles(5));*/
    }

    /**
     * 通用设置
     * @param modelAndView
     */
    private void setNormal(ModelAndView modelAndView) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> map3 = new HashMap<>();
        map.put("author",blogProperties.getAuthor());
        map.put("shici",map3);
        map3.put("description",shiCiProperties.getDescription());
        map3.put("keyword",shiCiProperties.getKeyword());
        modelAndView.addObject("head",map);
    }
}
