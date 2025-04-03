package com.git.blog.api.controller.front;

import com.alibaba.fastjson2.JSON;
import com.git.blog.config.properties.BlogProperties;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private BlogProperties blogProperties;

    @ApiOperation(value = "健康检查")
    @GetMapping("/ok")
    public String ok(){
        return "ok";
    }

    @ApiOperation(value = "版本检查")
    @GetMapping("/version")
    public String version(){
        return "2022-09-27";
    }

    @ApiOperation(value = "版本检查")
    @GetMapping("/check/env/{key}")
    public String version(@PathVariable("key")String key){
        return environment.getProperty(key);
    }

    @ApiOperation(value = "版本检查")
    @GetMapping("/check/config")
    public String config(){
        return JSON.toJSONString(blogProperties);
    }

}
