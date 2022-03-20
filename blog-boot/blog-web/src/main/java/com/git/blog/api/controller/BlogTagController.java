package com.git.blog.api.controller;

import com.git.blog.commmon.ApiResponse;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.dto.blog.BlogTagDTO;
import com.git.blog.service.TagTypeService;
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
@RequestMapping("/api/tag")
@Validated
public class BlogTagController {

    @Autowired
    private TagTypeService tagTypeService;

    @ApiOperation(value = "新增标签")
    @PostMapping("/addTag")
    public ApiResponse<Boolean> addTag(@RequestBody BlogTagDTO blogTagDTO){
        blogTagDTO.setCreateUid(AuthTheadLocal.get());
        Boolean result = tagTypeService.addTag(blogTagDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "修改标签")
    @PostMapping("/updateTag")
    public ApiResponse<Boolean> updateTag(@RequestBody @Valid BlogTagDTO blogTagDTO){
        blogTagDTO.setCreateUid(AuthTheadLocal.get());
        Boolean result = tagTypeService.updateTag(blogTagDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "新增标签")
    @GetMapping("/deleteTag/{id}")
    public ApiResponse<Boolean> deleteTag(@PathVariable("id") Long id){
        Boolean result = tagTypeService.deleteTag(id);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "新增标签")
    @GetMapping("/deleteTags")
    public ApiResponse<Boolean> deleteTags(@RequestBody List<Long> ids){
        Boolean result = tagTypeService.deleteTags(ids);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "获取所有标签")
    @GetMapping("/queryAllTags")
    public ApiResponse<List<BlogTagDTO>> queryAllTags(){
        List<BlogTagDTO> result = tagTypeService.queryAllTags();
        return ApiResponse.ok(result);
    }

}
