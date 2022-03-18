package com.git.blog.feign.tx;

import com.git.blog.dto.notice.NoticeRequest;
import com.git.blog.feign.fallback.WxWorkWebHookFeignFallBack;
import com.git.blog.dto.notice.WxWorkWebHookRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 企业微信发送
 * @author authorZhao
 * @since 2021-01-15
 */
@FeignClient(value = "WxWorkWebHookFeign",url = "https://qyapi.weixin.qq.com/cgi-bin/webhook",fallbackFactory = WxWorkWebHookFeignFallBack.class )
public interface WxWorkWebHookFeign {

    /**
     * 企业维机器人通知发送，https://qyapi.weixin.qq.com/cgi-bin/webhook
     * @param key key
     * @param param 参数，类型见官网文档
     */
    @PostMapping("/send")
    String send(@RequestParam("key")String key,@RequestBody WxWorkWebHookRequest param);

    @PostMapping("/send")
    String sendNoticeRequest(@RequestParam("key")String key,@RequestBody NoticeRequest param);
}
