package com.git.blog.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.ApiResponse;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.Permission;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.user.*;
import com.git.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
@RequestMapping("/api/user")
@Slf4j
@Api(tags = "[用户UserController]")
@Permission
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录")
    @GetMapping("/login_v2")
    @Permission(open = false)
    public void loginV2(@RequestParam("code")String code, @RequestParam("state") String state, HttpServletResponse response){
        log.info("回调进入code={},state={}",code,state);
        String redirectUrl = userService.loginV2(code,state);
        log.info("回调结束redirectUrl:{}",redirectUrl);
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ApiResponse<UserVO> login(@RequestBody @Valid LoginDTO loginDTO){
        return ApiResponse.ok(userService.login(loginDTO));
    }


    @ApiOperation(value = "获取微信二维码")
    @PostMapping("/getUrl")
    @Permission(open = false)
    public ApiResponse<String> getUrl(@RequestParam("redirectUrl")String redirectUrl){
        return ApiResponse.ok(userService.getGenCode(redirectUrl));
    }

    @ApiOperation(value = "修改用户")
    @PostMapping("/update")
    public ApiResponse<Boolean> update(@RequestBody UserDTO userDTO){

        return ApiResponse.ok(userService.update(userDTO));
    }

    @ApiOperation(value = "修改用户")
    @PostMapping("/updateSelf")
    public ApiResponse<Boolean> updateSelf(@RequestBody UserDTO userDTO){
        //校验长度
        userDTO.setUid(AuthTheadLocal.get());

        return ApiResponse.ok(userService.update(userDTO));
    }

    @ApiOperation(value = "用户分页查询")
    @PostMapping("/page")
    public ApiResponse<Page<UserVO>> page(@RequestBody UserQueryDTO userQueryDTO){
        Page<UserVO> result = userService.page(userQueryDTO);
        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "用户审核通过")
    @GetMapping("/approved/{uid}")
    public ApiResponse<Boolean> approved(@PathVariable("uid") Long uid){
        return ApiResponse.ok(userService.approved(uid));
    }

    @ApiOperation(value = "批量修改用户")
    @PostMapping("/batchUpdate")
    public ApiResponse<Boolean> batchUpdate(@RequestBody @Valid UserBatchDTO userBatchDTO){
        return ApiResponse.ok(userService.UserBatchDTO(userBatchDTO));
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getUserInfo/{uid}")
    public ApiResponse<UserVO> getUserInfo(@PathVariable("uid") Long uid){
        UserVO userInfo = userService.getUserInfo(uid);
        return userInfo==null?ApiResponse.error():ApiResponse.ok(userInfo);
    }

    @ApiOperation(value = "获取用户自身信息")
    @GetMapping("/getUserMySelf")
    @Permission(open = false)
    public ApiResponse<UserMenuVO> getUserMySelf(){
        Long uid = AuthTheadLocal.get();
        UserMenuVO userMenuVO = userService.getUserMySelf(uid);
        return ApiResponse.ok(userMenuVO);
    }

    @ApiOperation(value = "获取用户菜单树")
    @GetMapping("/getUserMenu")
    @Permission(open = false)
    public ApiResponse<List<MenuTreeVO>> getUserMenu(){
        Long uid = AuthTheadLocal.get();
        return ApiResponse.ok(userService.getUserMenu(uid));
    }


    @ApiOperation(value = "测试专用登录")
    @GetMapping("/testLogin")
    @Permission(open = false)
    public ApiResponse<UserVO> login(@RequestParam("nickName")String nickName){
        log.info("测试登录方法，nickName={}",nickName);
        UserVO userVO = userService.loginByNickName(nickName);
        log.info("测试登录方法");
        return userVO == null? ApiResponse.error():
                ApiResponse.ok(userVO);
    }

    @ApiOperation(value = "用户审核通过")
    @GetMapping("/delete/{uid}")
    public ApiResponse<Boolean> delete(@PathVariable("uid") Long uid){
        return ApiResponse.ok(userService.delete(uid));
    }

    @ApiOperation(value = "用户下拉")
    @GetMapping("/select")
    @Permission(open = false)
    public ApiResponse<List<UserVO>> select(){
        return ApiResponse.ok(userService.select(null));
    }

}

