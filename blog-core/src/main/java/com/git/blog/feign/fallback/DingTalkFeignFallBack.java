package com.git.blog.feign.fallback;

import com.git.blog.dto.notice.NoticeRequest;
import com.git.blog.feign.al.DingTalkFeign;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Map;

/**
 * @author authorZhao
 * @since 2021-01-15
 */
public class DingTalkFeignFallBack implements FallbackFactory<DingTalkFeign> {

    @Override
    public DingTalkFeign create(Throwable cause) {
        return new DingTalkFeign() {
            @Override
            public String send(String token, Long timestamp, String sign, Map param) {
                return null;
            }

            @Override
            public String sendNoticeRequest(String token, Long timestamp, String sign, NoticeRequest noticeRequest) {
                return null;
            }
        };
    }
}
