package com.git.blog.dto.wx;

import com.git.blog.dto.model.entity.User;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author authorZhao
 * @since 2020-12-28
 */
@Data
public class WxUserInfo {


    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    private String openid;

    /**
     * 普通用户昵称
     */
    private String nickname;

    /**
     * 普通用户性别，1为男性，2为女性
     */
    private int sex;


    /**
     * 普通用户个人资料填写的省份
     */
    private String province;

    /**
     * 普通用户个人资料填写的城市
     */
    private String city;

    /**
     * 国家，如中国为CN
     */
    private String country;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     */
    private String headimgurl;

    /**
     * 坑逼的tx啊，
     */
    private String language;

    /**
     * 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     */
    private List<String> privilege;

    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     */
    private String unionid;

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;


    public boolean isSuccess(){
        return StringUtils.isBlank(errmsg);
    }

    public boolean isError(){
        return StringUtils.isNotBlank(errmsg);
    }

    public User convertWxUser(){
        User user = new User();
        user.setNickname(this.getNickname());
        user.setPassword(this.getOpenid());
        user.setAvatar(this.getHeadimgurl());
        return user;
    }

}
