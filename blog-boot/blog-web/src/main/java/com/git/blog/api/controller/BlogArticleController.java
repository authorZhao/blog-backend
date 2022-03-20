package com.git.blog.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.ApiResponse;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.dto.blog.BlogArticleDTO;
import com.git.blog.dto.blog.BlogArticleDetailVO;
import com.git.blog.dto.blog.BlogArticlePageDTO;
import com.git.blog.dto.blog.BlogArticleVO;
import com.git.blog.service.ArticleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @author authorZhao
 */
@RestController
@RequestMapping("/api/article")
@Validated
public class BlogArticleController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "文章分页")
    @PostMapping("/pageArticle")
    public ApiResponse<Page<BlogArticleVO>> pageArticle(@RequestBody BlogArticlePageDTO blogArticlePageDTO){
        if(blogArticlePageDTO.getPageSize()>=50){
            return ApiResponse.ok();
        }
        Page<BlogArticleVO> result = articleService.pageArticle(blogArticlePageDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "查询所有")
    @PostMapping("/listArticle")
    public ApiResponse<List<BlogArticleVO>> listArticle(@RequestBody BlogArticlePageDTO blogArticlePageDTO){
        blogArticlePageDTO.setCurrent(1);
        blogArticlePageDTO.setPageSize(10000);
        return ApiResponse.ok(articleService.pageArticle(blogArticlePageDTO).getRecords());
    }

    @ApiOperation(value = "新增和修改")
    @PostMapping("/saveOrUpdate")
    public ApiResponse<Boolean> saveOrUpdate(@RequestBody @Valid BlogArticleDTO blogArticlePageDTO){
        blogArticlePageDTO.setUserId(AuthTheadLocal.get());
        Boolean result = articleService.saveOrUpdate(blogArticlePageDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "删除文章")
    @GetMapping("/deleteArticle/{id}")
    public ApiResponse<Boolean> deleteArticle(@PathVariable("id") Long id){
        Boolean result = articleService.deleteArticle(id);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "恢复文章")
    @GetMapping("/recoveryArticle/{id}")
    public ApiResponse<Boolean> recoveryArticle(@PathVariable("id") Long id){
        Boolean result = articleService.recoveryArticle(id);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "详情")
    @GetMapping("/detailArticle/{id}")
    public ApiResponse<BlogArticleDetailVO> detailArticle(@PathVariable("id") Long id){
        BlogArticleDetailVO result = articleService.detailArticle(id);
        return ApiResponse.ok(result);
    }

    private boolean isInit = false;
    @ApiOperation(value = "重新初始化博客")
    @GetMapping("/init")
    public ApiResponse<Boolean> detailArticle(){
        //单机避免重复点击
        if(isInit)ApiResponse.ok(Boolean.TRUE);
        try {
            isInit = true;
            articleService.init();
        }finally {
            isInit = false;
        }
        return ApiResponse.ok(Boolean.TRUE);
    }


}
