package com.git.blog.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.ApiResponse;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.Permission;
import com.git.blog.dto.config.KvData;
import com.git.blog.dto.config.StrKV;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.user.*;
import com.git.blog.service.ConfigService;
import com.git.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author authorZhao
 * @since 2020-12-24
 */
@RestController
@RequestMapping("/api/admin/config")
@Slf4j
@Api(tags = "[用户UserController]")
public class AdminConfigController {

    @Autowired
    private ConfigService configService;

    @ApiOperation(value = "登录")
    @PostMapping("/updateKV")
    public ApiResponse<KvData> updateKV(@RequestBody @Valid StrKV strKV){
        return ApiResponse.ok(configService.updateKV(strKV));
    }

}

