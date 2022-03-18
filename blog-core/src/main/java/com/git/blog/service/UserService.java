package com.git.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.user.*;
import com.git.blog.dto.wx.WxAccessTokenRspDTO;
import com.git.blog.dto.model.entity.Menu;
import com.git.blog.dto.model.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
public interface UserService {

    /**
     * 返回二维码请求地址
     * @param code
     * @return
     */
    @Deprecated
    WxAccessTokenRspDTO getWxToken(String code);

    /**
     * 生成二维码请求路径，此前这个是redirectUrl=http://localhost:9527/#/auth-redirect,但是这个#号会被微信截取
     * 采用逗号,分隔
     * @param redirectUrl
     * @return
     */
    String getGenCode(String redirectUrl);

    /**
     * 登录方法
     * @param code
     * @param state  http://localhost:9527/,/auth-redirect 采用逗号分隔，注意
     * @return String
     */
    String loginV2(String code, String state);

    /**
     * 改
     * @param userDTO
     * @return
     */
    Boolean update(UserDTO userDTO);

    /**
     * 用户审核通过
     * @param uid
     * @return
     */
    Boolean approved(Long uid);

    /**
     * 测试专用登录方法
     * @param nickName
     * @return
     */
    UserVO loginByNickName(String nickName);

    /**
     * 获取用户详情
     * @param uid
     * @return
     */
    UserVO getUserInfo(Long uid);

    /**
     * 删除用户
     * @param uid
     * @return
     */
    Boolean delete(Long uid);

    /**
     * 用户分页查询
     * @param userQueryDTO
     * @return
     */
    Page<UserVO> page(UserQueryDTO userQueryDTO);

    /**
     * 获取用户菜单列表
     * @param uid
     * @return
     */
    List<MenuTreeVO> getUserMenu(Long uid);

    /**
     * 活用用户所有的菜单，不是树形式的
     * @param uid
     * @return
     */
    List<Menu> getUserAllMenu(Long uid);

    /**
     * 用户下拉列表
     * @param roleName
     * @return
     */
    List<UserVO> select(String roleName);

    /**
     * 获取用户自身信息
     * @param uid
     * @return
     */
    UserMenuVO getUserMySelf(Long uid);

    /**
     * 获取所有管理员，管理员不能随便创建
     * @return
     */
    List<User> getAdmin();

    /**
     * 根据uid获取userMap
     * @param uidList 用户uid集合，获取不到禁用用户
     * @return
     */
    Map<Long,User> getUserMapByUid(List<Long> uidList);

    /**
     * 根据id获取
     * @param uid
     * @return
     */
    User getUserById(Long uid);

    /**
     * 根据uid判断是不是管理员
     * @param uid
     * @return
     */
    boolean isAdmin(Long uid);

    /**
     * 批量修改用户
     * @param userBatchDTO
     * @return
     */
    Boolean UserBatchDTO(UserBatchDTO userBatchDTO);

    /**
     * 登录
     * @param loginDTO
     * @return
     */
    UserVO login(LoginDTO loginDTO);
}
