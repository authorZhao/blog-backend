package com.git.blog.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.ApiResult;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.Permission;
import com.git.blog.dto.role.RoleDTO;
import com.git.blog.dto.role.RoleQueryDTO;
import com.git.blog.dto.role.RoleVO;
import com.git.blog.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@RestController
@RequestMapping("/api/role")
@Slf4j
@Api(tags = "[角色RoleController]")
@Permission
public class RoleController {

    @Autowired
    private RoleService roleService;


    @ApiOperation(value = "新增角色")
    @PostMapping("/save")
    public ApiResult<Boolean> save(@RequestBody @Valid RoleDTO roleDTO){
        if(roleDTO==null){
            return ApiResult.<Boolean>builder().result(ApiResult.RESULT_FAIL).data(false).build();
        }
        roleDTO.setCreateUid(AuthTheadLocal.get());
        Boolean result = roleService.save(roleDTO);
        return ApiResult.<Boolean>builder().result(ApiResult.RESULT_SUCCESS).data(result).build();
    }

    @ApiOperation(value = "修改角色")
    @PostMapping("/update")
    public ApiResult<Boolean> update(@RequestBody RoleDTO roleDTO){
        if(Objects.isNull(roleDTO) || Objects.isNull(roleDTO.getRoleId())){
            return ApiResult.<Boolean>builder().result(ApiResult.RESULT_SUCCESS).data(Boolean.FALSE).build();
        }
        roleDTO.setCreateUid(AuthTheadLocal.get());
        Boolean result = roleService.update(roleDTO);
        return ApiResult.<Boolean>builder().result(ApiResult.RESULT_SUCCESS).data(result).build();
    }

    @ApiOperation(value = "角色列表查询")
    @PostMapping("/list")
    @Permission(open = false)
    public ApiResult<List<RoleVO>> list(@RequestBody RoleQueryDTO roleQueryDTO){
        List<RoleVO> list = roleService.list(roleQueryDTO);
        return ApiResult.<List<RoleVO>>builder().result(ApiResult.RESULT_SUCCESS).data(list).build();
    }

    @ApiOperation(value = "角色分页查询")
    @PostMapping("/page")
    public ApiResult<Page<RoleVO>> page(@RequestBody RoleQueryDTO roleQueryDTO){
        Page<RoleVO> result = roleService.page(roleQueryDTO);
        return ApiResult.<Page<RoleVO>>builder().result(ApiResult.RESULT_SUCCESS).data(result).build();
    }


    @ApiOperation(value = "根据id删除角色")
    @GetMapping("/delete/{roleId}")
    public ApiResult<Boolean> delete(@PathVariable("roleId") Long roleId){
        Boolean result = roleService.delete(roleId);
        return ApiResult.<Boolean>builder().result(ApiResult.RESULT_SUCCESS).data(result).build();
    }

    @ApiOperation(value = "根据id获取角色详情")
    @GetMapping("/detail/{roleId}")
    public ApiResult<RoleVO> detail(@PathVariable("roleId") Long roleId){
        RoleVO menuVO = roleService.detail(roleId);
        return ApiResult.<RoleVO>builder().result(ApiResult.RESULT_SUCCESS).data(menuVO).build();
    }

}

