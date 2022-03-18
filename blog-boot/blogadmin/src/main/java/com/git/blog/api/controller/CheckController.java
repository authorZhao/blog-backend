package com.git.blog.api.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

/**
 * @author authorZhao
 * @since 2020-12-28
 */

@RestController
@RequestMapping("/api")
@Slf4j
public class CheckController {
    @Autowired
    private Environment environment;

    @ApiOperation(value = "健康检查")
    @GetMapping("/ok")
    public String ok(){
        return "ok";
    }

    @ApiOperation(value = "版本检查")
    @GetMapping("/version")
    public String version(){
        return "2021-03-03";
    }

}
