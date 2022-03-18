package com.git.blog.feign.al;

import com.git.blog.dto.notice.NoticeRequest;
import com.git.blog.feign.fallback.DingTalkFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 钉钉通知发送
 * @author authorZhao
 * @since 2021-01-15
 */
@FeignClient(value = "DingTalkFeign",url = "https://oapi.dingtalk.com/robot",fallbackFactory = DingTalkFeignFallBack.class )
public interface DingTalkFeign {

    /**
     * 钉钉通知发送，https://oapi.dingtalk.com/robot/send?access_token=XXXXXX&timestamp=XXX&sign=XXX
     * @param token token
     * @param timestamp 时间戳
     * @param sign 签名
     * @param param 参数，类型见官网文档
     */
    @PostMapping("/send")
    String send(@RequestParam("access_token")String token,
              @RequestParam("timestamp")Long timestamp,
              @RequestParam("sign")String sign,
              @RequestBody Map param);

    /**
     * 发送通知，同上，map转化为具体对象
     * @param token
     * @param timestamp
     * @param sign
     * @param noticeRequest
     * @return
     */
    @PostMapping("/send")
    String sendNoticeRequest(@RequestParam("access_token")String token,
                @RequestParam("timestamp")Long timestamp,
                @RequestParam("sign")String sign,
                @RequestBody NoticeRequest noticeRequest);
}
