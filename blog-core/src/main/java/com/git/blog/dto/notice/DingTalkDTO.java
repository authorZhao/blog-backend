package com.git.blog.dto.notice;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author authorZhao
 * @since 2021-01-15
 */
@Data
public class DingTalkDTO {

    public static final String TEXT="{\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"},\"at\":{\"atMobiles\":[\"%s\"],\"isAtAll\":%s}}";


    /**
     * 文本消息类型
     */
    public static class DingTalkTextDTO{

    }

    /**
     * 钉钉文本通知构建
     * @param content 文本内容,里面可用@电话号码
     * @param mobiles @的手机号码集合
     * @param isAtAll 是否发给所有人，只有在手机号码集合为空才有
     * @return {
     * 	"at": {
     * 		"isAtAll": false,
     * 		"atMobiles": "15671689898,15671689899"
     *        },
     * 	"text": {
     * 		"content": "test,我就是我, @15671689973 是不一样的烟火"
     *    },
     * 	"msgtype": "text"
     * }
     */
    public static Map getTextParam(String content, List<String> mobiles,Boolean isAtAll){
        Map<String,Object> map = new HashMap<>();
        map.put("msgtype","text");
        Map<String,Object> text = new HashMap<>();
        text.put("content",content);
        map.put("text",text);
        Map<String,Object> at = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mobiles)){
            at.put("atMobiles",mobiles);
        }
        at.put("isAtAll", CollectionUtils.isEmpty(mobiles) || BooleanUtils.isTrue(isAtAll));
        map.put("at",at);
        return map;
    }



    /**
     * md形式内容组装
     * @param content md内容
     * @param mobiles @的手机号码
     * @param isAtAll 是否发给所有人，只有在手机号码集合为空才有
     * @return {
     *         "msgtype": "markdown",
     *             "markdown": {
     *         "title":"杭州天气",
     *                 "text": "#### 杭州天气 @150XXXXXXXX \n> 9度，西北风1级，空气良89，相对温度73%\n> ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n> ###### 10点20分发布 [天气](https://www.dingtalk.com) \n"
     *     },
     *         "at": {
     *         "atMobiles": [
     *         "150XXXXXXXX"
     *           ],
     *         "isAtAll": false
     *     }
     *     }
     */
    public static Map getMarkDownParam(String title,String content, List<String> mobiles,Boolean isAtAll){
        Map<String,Object> map = new HashMap<>();
        map.put("msgtype","markdown");
        Map<String,Object> markdown = new HashMap<>();
        markdown.put("title",title);
        markdown.put("text",content);
        map.put("markdown",markdown);
        Map<String,Object> at = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mobiles)){
            at.put("atMobiles",mobiles);
        }
        at.put("isAtAll", CollectionUtils.isEmpty(mobiles) || BooleanUtils.isTrue(isAtAll));
        map.put("at",at);
        return map;
    }

    /**
     * 链接类型，没有at
     * @param title 标题
     * @param text 内容
     * @param picUrl 图片链接
     * @param messageUrl 跳转路径
     * @return {
     *     "msgtype": "link",
     *     "link": {
     *         "text": "这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林",
     *         "title": "时代的火车向前开",
     *         "picUrl": "",
     *         "messageUrl": "https://www.dingtalk.com/s?__biz=MzA4NjMwMTA2Ng==&mid=2650316842&idx=1&sn=60da3ea2b29f1dcc43a7c8e4a7c97a16&scene=2&srcid=09189AnRJEdIiWVaKltFzNTw&from=timeline&isappinstalled=0&key=&ascene=2&uin=&devicetype=android-23&version=26031933&nettype=WIFI"
     *     }
     * }
     */
    public static Map getLinkParam(String title,String text,String picUrl,String messageUrl){
        Map<String,Object> map = new HashMap<>();
        map.put("msgtype","link");
        Map<String,Object> link = new HashMap<>();
        link.put("title",title);
        link.put("text",text);
        link.put("picUrl",picUrl);
        link.put("messageUrl",messageUrl);
        map.put("link",link);
        return map;
    }

    public static void main(String[] args) {
        Map textParam = getTextParam("test,我就是我, @15671689973 是不一样的烟火", Lists.newArrayList("15671689973", "15671689974"), null);

        String s = JSON.toJSONString(textParam);

        System.out.println("s = " + s);
    }
}
