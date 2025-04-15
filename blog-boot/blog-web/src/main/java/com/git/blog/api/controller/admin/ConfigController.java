package com.git.blog.api.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.ApiResponse;
import com.git.blog.dto.config.*;
import com.git.blog.service.ConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/config")
@Validated
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @PostMapping("/add")
    public ApiResponse<Boolean> add(@Valid @RequestBody AddConfigDTO dto) {
        return ApiResponse.ok(configService.addConfig(dto));
    }

    @PostMapping("/update")
    public ApiResponse<Boolean> update(@Valid @RequestBody UpdateConfigDTO dto) {
        return ApiResponse.ok(configService.updateConfig(dto));
    }

    @GetMapping("/delete/{id}")
    public ApiResponse<Boolean> delete(@PathVariable(name = "id") Integer id) {
        return ApiResponse.ok(configService.logicDelete(id));
    }

    @GetMapping("/{id}")
    public ApiResponse<ConfigDetailDTO> detail(@PathVariable Integer id) {
        return ApiResponse.ok(configService.getDetail(id));
    }

    @PostMapping("/page")
    public ApiResponse<Page<ConfigPageDTO>> page(@Valid @RequestBody ConfigPageQueryDTO dto) {
        return ApiResponse.ok(configService.getPage(dto));
    }
}
