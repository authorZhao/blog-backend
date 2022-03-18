package com.git.blog.service;

import com.git.blog.dto.wx.WxAccessTokenRspDTO;
import com.git.blog.dto.wx.WxUserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信接口处理
 * @author authorZhao
 * @since 2020-12-29
 */
public interface WxOpenService {

    /**
     * 根据openId和accessToken获取用户信息
     * @param accessToken
     * @param openid
     * @return
     */
    @GetMapping(value = "/userinfo",consumes = "application/json;charset=UTF-8")
    WxUserInfo getUserInfo(@RequestParam("access_token") String accessToken, @RequestParam("openid") String openid);


    /**
     * 校验token是否有效,该接口只有错位信息，没有具体用户信息
     * @param accessToken
     * @return
     */
    @GetMapping("/auth")
    WxAccessTokenRspDTO checkAccessToken(@RequestParam("access_token") String accessToken);


    /**
     * 获取token  https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     * @param appId
     * @param secret
     * @param code
     * @param grantType
     * @return
     */
    @GetMapping("/oauth2/access_token")
    WxAccessTokenRspDTO getAccessToken(@RequestParam("appid") String appId,
                                       @RequestParam("secret") String secret,
                                       @RequestParam("code") String code,
                                       @RequestParam("grant_type") String grantType);


    /**
     * 刷新token https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
     * @param appId
     * @param grantType
     * @param refreshToken
     * @return
     */
    @GetMapping("/oauth2/refresh_token")
    WxAccessTokenRspDTO refreshAccessToken(@RequestParam("appid") String appId,
                              @RequestParam("grant_type") String grantType,
                              @RequestParam("refresh_token") String refreshToken);
}
