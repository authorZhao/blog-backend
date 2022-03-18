package com.git.blog.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.ApiResponse;
import com.git.blog.commmon.ApiResult;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.Permission;
import com.git.blog.dto.menu.MenuDTO;
import com.git.blog.dto.menu.MenuQueryDTO;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.menu.MenuVO;
import com.git.blog.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@RestController
@RequestMapping("/api/menu")
@Slf4j
@Api(tags = "[菜单MenuController]")
@Validated
@Permission(message = "没有菜单相关权限")
public class MenuController {

    @Autowired
    private MenuService menuService;



    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public ApiResponse<Boolean> save(@RequestBody @Valid MenuDTO menuDTO){
        if(menuDTO!=null){
            menuDTO.setCreateUid(AuthTheadLocal.get());
        }
        Boolean result = menuService.save(menuDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "修改菜单")
    @PostMapping("/update")
    public ApiResponse<Boolean> update(@RequestBody MenuDTO menuDTO){
        if(menuDTO!=null){
            menuDTO.setCreateUid(AuthTheadLocal.get());
        }
        if(Objects.isNull(menuDTO) || Objects.isNull(menuDTO.getMenuId())){
            return ApiResponse.ok(Boolean.FALSE);
        }
        Boolean result = menuService.update(menuDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "菜单列表查询")
    @PostMapping("/list")
    @Permission(open = false)
    public ApiResult<List<MenuVO>> list(@RequestBody MenuQueryDTO menuQueryDTO){
        List<MenuVO> list = menuService.list(menuQueryDTO);
        return ApiResult.<List<MenuVO>>builder().result(ApiResult.RESULT_SUCCESS).data(list).build();
    }

    @ApiOperation(value = "菜单分页查询")
    @PostMapping("/page")
    public ApiResult<Page<MenuVO>> page(@RequestBody MenuQueryDTO menuQueryDTO){
        Page<MenuVO> result = menuService.page(menuQueryDTO);
        return ApiResult.<Page<MenuVO>>builder().result(ApiResult.RESULT_SUCCESS).data(result).build();
    }

    @ApiOperation(value = "个人菜单树查询,带分页的")
    @PostMapping("/treeList")
    @Permission(open = false)
    public ApiResult<Page<MenuTreeVO>> treeList(@RequestBody MenuQueryDTO menuQueryDTO){
        Page<MenuTreeVO> menuTreeVo = menuService.treeList(menuQueryDTO);
        return ApiResult.<Page<MenuTreeVO>>builder().result(ApiResult.RESULT_SUCCESS).data(menuTreeVo).build();
    }

    @ApiOperation(value = "所有菜单树查询")
    @GetMapping("/treeListAll")
    @Permission(open = false)
    public ApiResult<List<MenuTreeVO>> treeListAll(){
        List<MenuTreeVO> menuTreeVo = menuService.treeListAll();
        return ApiResult.<List<MenuTreeVO>>builder().result(ApiResult.RESULT_SUCCESS).data(menuTreeVo).build();
    }

    @ApiOperation(value = "根据id删除菜单")
    @GetMapping("/delete/{menuId}")
    public ApiResult<Boolean> delete(@PathVariable("menuId") Long menuId){
        Boolean result = menuService.delete(menuId);
        return ApiResult.<Boolean>builder().result(ApiResult.RESULT_SUCCESS).data(result).build();
    }

    @ApiOperation(value = "根据id获取详情")
    @GetMapping("/detail/{menuId}")
    public ApiResult<MenuVO> detail(@PathVariable("menuId") Long menuId){
        MenuVO menuVO = menuService.detail(menuId);
        return ApiResult.<MenuVO>builder().result(ApiResult.RESULT_SUCCESS).data(menuVO).build();
    }

}

