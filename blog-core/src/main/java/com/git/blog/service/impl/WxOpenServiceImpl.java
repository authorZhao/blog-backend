package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.blog.dto.wx.WxAccessTokenRspDTO;
import com.git.blog.dto.wx.WxUserInfo;
import com.git.blog.feign.WxOpenLoginFeign;
import com.git.blog.service.WxOpenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author authorZhao
 * @since 2020-12-29
 */

@Service
@Slf4j
public class WxOpenServiceImpl implements WxOpenService {

    @Autowired
    private WxOpenLoginFeign wxOpenLoginFeign;

    @Override
    public WxUserInfo getUserInfo(String accessToken, String openid) {
        String result = wxOpenLoginFeign.getUserInfo(accessToken,openid);
        if(StringUtils.isBlank(result)){
            log.warn("wxOpenLoginFeign.getUserInfo：{}",result);
            return null;
        }
        return JSON.parseObject(result,WxUserInfo.class);
    }

    @Override
    public WxAccessTokenRspDTO checkAccessToken(String accessToken) {
        String result = wxOpenLoginFeign.checkAccessToken(accessToken);
        if(StringUtils.isBlank(result)){
            log.warn("wxOpenLoginFeign.checkAccessToken：{}",result);
            return null;
        }
        return JSON.parseObject(result,WxAccessTokenRspDTO.class);
    }

    @Override
    public WxAccessTokenRspDTO getAccessToken(String appId, String secret, String code, String grantType) {
        String result = wxOpenLoginFeign.getAccessToken(appId,secret,code,grantType);
        if(StringUtils.isBlank(result)){
            log.warn("wxOpenLoginFeign.getAccessToken：{}",result);
            return null;
        }
        return JSON.parseObject(result,WxAccessTokenRspDTO.class);
    }

    @Override
    public WxAccessTokenRspDTO refreshAccessToken(String appId, String grantType, String refreshToken) {
        String result = wxOpenLoginFeign.refreshAccessToken(appId,grantType,refreshToken);
        if(StringUtils.isBlank(result)){
            log.warn("wxOpenLoginFeign.refreshAccessToken：{}",result);
            return null;
        }
        return JSON.parseObject(result,WxAccessTokenRspDTO.class);
    }
}
