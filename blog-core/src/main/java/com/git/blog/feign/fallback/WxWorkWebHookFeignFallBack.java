package com.git.blog.feign.fallback;

import com.git.blog.dto.notice.NoticeRequest;
import com.git.blog.feign.tx.WxWorkWebHookFeign;
import com.git.blog.dto.notice.WxWorkWebHookRequest;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 企业微信
 * @author authorZhao
 * @since 2021-03-11
 */
public class WxWorkWebHookFeignFallBack implements FallbackFactory<WxWorkWebHookFeign> {

    @Override
    public WxWorkWebHookFeign create(Throwable cause) {
        return new WxWorkWebHookFeign(){

            @Override
            public String send(String key, WxWorkWebHookRequest param) {
                return null;
            }

            @Override
            public String sendNoticeRequest(String key, NoticeRequest param) {
                return null;
            }
        };
    }
}
