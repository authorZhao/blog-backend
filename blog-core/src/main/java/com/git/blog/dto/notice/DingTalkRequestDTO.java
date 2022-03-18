package com.git.blog.dto.notice;

import com.alibaba.fastjson.JSON;
import com.git.blog.commmon.enums.NoticeTypeEnum;
import com.git.blog.util.SignUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 钉钉通知类型整理，常用就文本和link等
 * @author authorZhao
 * @since 2021-03-11
 */
@Data
@Accessors(chain = true)
public class DingTalkRequestDTO extends NoticeRequest{

    private final static String path = "https://oapi.dingtalk.com/robot/send";


    /**
     * 消息类型
     */
    private String msgtype;
    /**
     * 文本类型
     */
    private DingTalkRequestDTO.TextMsg text;

    /**
     * text和markdown都有
     */
    private DingTalkRequestDTO.At at;

    /**
     * 链接类型
     */
    private DingTalkRequestDTO.Link link;

    /**
     * markdown类型
     */
    private DingTalkRequestDTO.MarkdownMsg markdown;
    /**
     * 整体跳转
     */
    private DingTalkRequestDTO.ActionCard actionCard;
    /**
     * 多个跳转
     */
    private DingTalkRequestDTO.FeedCard feedCard;

    /**
     * 暂时写死
     */
    private static final String DING_TALK_TOKEN = "ee57b5318c68d2441491f312ba1457c3d00a28ba053e19398fd5ab05ea5d5e57";
    private static final String DING_TALK_SECRET = "SEC8cfdcc7684ba69600c1b7dfc195e4ee4f3aed0905b452289b0efd96319264dfd";


    @Override
    public NoticeTypeEnum getNoticeType() {
        return NoticeTypeEnum.DING_DING;
    }

    public String send() {
        Map<String,Object> urlParam = new HashMap();
        long time = System.currentTimeMillis();
        urlParam.put("access_token",DING_TALK_TOKEN);
        urlParam.put("timestamp",time);
        urlParam.put("sign",SignUtil.dingTalkSign(time,DING_TALK_SECRET));


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String content = JSON.toJSONString(this);
        HttpEntity<String> request = new HttpEntity<>(content, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(path + "?access_token={access_token}&timestamp={timestamp}&sign={sign}", HttpMethod.POST, request, String.class,urlParam);
        return responseEntity.getBody();
    }


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

    public DingTalkRequestDTO text(String content){
        return this.setMsgtype("text").setText(new TextMsg(content));
    }

    public DingTalkRequestDTO at(List<String> atMobiles,Boolean isAtAll){
        if(CollectionUtils.isNotEmpty(atMobiles)){
            atMobiles = atMobiles.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }
        if(CollectionUtils.isEmpty(atMobiles)){
            atMobiles = null;
        }
        return this.setMsgtype("at").setAt(new At(atMobiles,isAtAll));
    }

    public DingTalkRequestDTO markdown(String title,String text){
        return this.setMsgtype("markdown").setMarkdown(new MarkdownMsg(title,text));
    }

    public DingTalkRequestDTO link(String text,String title,String picUrl,String messageUrl){
        return this.setMsgtype("link").setLink(new Link(text,title,picUrl,messageUrl));
    }


    @Data
    @AllArgsConstructor
    public static class TextMsg {
        /**
         * 文本内容里面可以使用@电话号码@别人
         */
        private String content;
    }

    @Data
    @AllArgsConstructor
    public static class At {
        private List<String> atMobiles;
        private Boolean isAtAll = false;
    }

    @Data
    @AllArgsConstructor
    public static class Link {
        private String text;
        private String title;
        private String picUrl;
        private String messageUrl;
    }


    @Data
    @AllArgsConstructor
    public static class MarkdownMsg {
        private String text;
        private String title;
    }

    @Data
    @AllArgsConstructor
    public static class ActionCard {
        private String title;
        private String text;
        private String btnOrientation;
        private String singleTitle;
        private String singleURL;
    }

    @Data
    @AllArgsConstructor
    public static class FeedCard {
        private List<Link> links;
    }



    public static void main(String[] args) {

        String result = new DingTalkRequestDTO().link("测试连接，请审核", "新用户审核通知", null, "www.baidu.com").send();
        System.out.println("result = " + result);

    }

}
