package com.git.blog.dto.notice;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.git.blog.commmon.enums.NoticeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 林源
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class WxWorkWebHookRequest extends NoticeRequest{

    private final static String path = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    private String msgtype;
    private TextMsg text;
    private MarkdownMsg markdown;
    private ImageMsg image;
    private NewsMsg news;
    private FileMsg file;


    public WxWorkWebHookResponse send(String key) {
        String url = String.format(path, key);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        String content = JSON.toJSONString(this);

        HttpEntity<String> request = new HttpEntity<>(content, headers);

        ResponseEntity<WxWorkWebHookResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, WxWorkWebHookResponse.class);
        return responseEntity.getBody();

    }

    @Override
    public NoticeTypeEnum getNoticeType() {
        return NoticeTypeEnum.WX_WORK;
    }

    @Data
    @AllArgsConstructor
    public static class TextMsg {
        private String content;
        private List<String> mentioned_list;
        private List<String> mentioned_mobile_list;
    }

    public static WxWorkWebHookRequest text(String content, List<String> mentionedList, List<String> mentionedMobileList) {
        if(CollectionUtils.isNotEmpty(mentionedMobileList)){
            mentionedMobileList = mentionedMobileList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        if(CollectionUtils.isEmpty(mentionedMobileList)){
            mentionedMobileList = null;
        }
        WxWorkWebHookRequest request = new WxWorkWebHookRequest();
        request.msgtype = "text";
        request.text = new TextMsg(content, mentionedList, mentionedMobileList);
        return request;
    }

    public static WxWorkWebHookRequest text(String content) {
        return text(content, null, null);
    }

    @Data
    @AllArgsConstructor
    public static class MarkdownMsg {
        private String content;
    }

    public static WxWorkWebHookRequest markdown(String content) {
        WxWorkWebHookRequest request = new WxWorkWebHookRequest();
        request.msgtype = "markdown";
        request.markdown = new MarkdownMsg(content);
        return request;
    }

    @Data
    @AllArgsConstructor
    public static class ImageMsg {
        private String base64;
        private String md5;
    }

    public static WxWorkWebHookRequest image(String base64, String md5) {
        WxWorkWebHookRequest request = new WxWorkWebHookRequest();
        request.msgtype = "image";
        request.image = new ImageMsg(base64, md5);
        return request;
    }

    @Data
    @AllArgsConstructor
    public static class NewsMsg {
        private List<Articles> articles;
    }

    public static WxWorkWebHookRequest news(List<Articles> articles) {
        WxWorkWebHookRequest request = new WxWorkWebHookRequest();
        request.msgtype = "news";
        request.news = new NewsMsg(articles);
        return request;
    }

    public static WxWorkWebHookRequest news(String title, String description, String url, String picurl) {
        return news(Lists.newArrayList(new Articles(title, description, url, picurl)));
    }


    @Data
    @AllArgsConstructor
    public static class Articles {
        private String title;
        private String description;
        private String url;
        private String picurl;
    }

    @Data
    @AllArgsConstructor
    public static class FileMsg {
        private String media_id;
    }

    public static WxWorkWebHookRequest file(String mediaId) {
        WxWorkWebHookRequest request = new WxWorkWebHookRequest();
        request.msgtype = "file";
        request.file = new FileMsg(mediaId);
        return request;
    }

    public static void main(String[] args) {
        String key = "cdebd701-fd69-4652-8b83-d1044d9eaecd";
        String m = "实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。\n>类型:<font color=\"comment\">用户反馈</font> \n>普通用户反馈:<font color=\"comment\">117例</font> \n>VIP用户反馈:<font color=\"comment\">15例</font>" +
                "" +
                "[链接测试](http://www.baidu.com)";
        WxWorkWebHookResponse send = WxWorkWebHookRequest.markdown(m).send(key);
        System.out.println(JSON.toJSONString(send));


//        WxWorkWebHookRequest.text("文本").send(key);
//        WxWorkWebHookRequest.text("文本@ :", Lists.newArrayList("@all"), null).send(key);
//        WxWorkWebHookRequest.markdown(m).send(key);
//        WxWorkWebHookRequest.news("标题", "描述", "https://pro.qunjielong.com/#/passport/login", "https://res0.shangshi360.com/ss/app/image/pc_case/case_logo006.png").send(key);
    }
}
