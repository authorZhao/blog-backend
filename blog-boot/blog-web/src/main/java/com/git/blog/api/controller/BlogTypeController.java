package com.git.blog.api.controller;

import com.git.blog.commmon.ApiResponse;
import com.git.blog.dto.blog.BlogTypeDTO;
import com.git.blog.service.TagTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * @author authorZhao
 */
@RestController
@RequestMapping("/api/type")
public class BlogTypeController {

    @Autowired
    private TagTypeService tagTypeService;

    @ApiOperation(value = "新增文章类型")
    @PostMapping("/addType")
    public ApiResponse<Boolean> addType(@RequestBody BlogTypeDTO blogTagDTO){
        Boolean result = tagTypeService.addType(blogTagDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "修改文章类型")
    @PostMapping("/updateType")
    public ApiResponse<Boolean> updateType(@RequestBody @Valid BlogTypeDTO blogTagDTO){
        Boolean result = tagTypeService.updateType(blogTagDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "删除文章类型")
    @GetMapping("/deleteType/{id}")
    public ApiResponse<Boolean> deleteType(@PathVariable("id") Long id){
        Boolean result = tagTypeService.deleteType(id);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "批量删除文章类型")
    @PostMapping("/deleteTypes")
    public ApiResponse<Boolean> deleteTypes(@RequestBody List<Long> ids){
        Boolean result = tagTypeService.deleteTypes(ids);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "查询正常type")
    @GetMapping("/queryAllTypes")
    public ApiResponse<List<BlogTypeDTO>> queryAllTypes(){
        List<BlogTypeDTO> result = tagTypeService.queryAllTypes();
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "查询所有type")
    @GetMapping("/listTypes")
    public ApiResponse<List<BlogTypeDTO>> listTypes(){
        List<BlogTypeDTO> result = tagTypeService.listTypes();
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "查询type tree")
    @PostMapping("/treeTypes")
    public ApiResponse<List<BlogTypeDTO>> treeTypes(){
        List<BlogTypeDTO> result = tagTypeService.treeTypes();
        return ApiResponse.ok(result);
    }

}
