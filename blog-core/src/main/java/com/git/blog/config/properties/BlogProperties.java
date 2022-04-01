package com.git.blog.config.properties;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author authorZhao
 * @since 2020-12-28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "open.blog.head")
public class BlogProperties implements Cloneable{
    /**标题*/
    private String title = "平原人的博客";
    /**作者*/
    private String author = "平原人";
    /**copyright*/
    private String copyright = "平原人";
    /**作者*/
    private String themeColor = "#ffffff";
    /**作者*/
    private String description = "大雪迷人眼";
    /**作者*/
    private String ogTitle = "平原人的博客";
    /**作者*/
    private String ogUrl = "http://www.pingyuanren.com";
    /**作者*/
    private String ogSiteName = "平原人的博客";
    /**作者*/
    private String ogDescription = "大雪迷人眼";
    /**作者*/
    private String ogLocale = "zh_CN";
    /**作者*/
    private String ogImage = "https://i.loli.net/2021/02/24/5O1day2nriDzjSu.png";
    /**作者*/
    private String articleAuthor = "平原人";
    /**作者*/
    private String twitterCard = "summary";
    /**作者*/
    private String twitterImage = "https://i.loli.net/2021/02/24/5O1day2nriDzjSu.png";
    /**作者*/
    private String shortcutIcon = "/butterfly/img/favicon.png";
    /**作者*/
    private String canonical = "http://www.pingyuanren.com";
    /**作者*/
    private String preconnect1 = "//cdn.jsdelivr.net";
    /**作者*/
    private String preconnect2 = "//busuanzi.ibruce.info";
    /**作者*/
    private String stylesheet1 = "/butterfly/css/index.css";
    /**作者*/
    private String stylesheet2 = "https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6/css/all.min.css";
    /**作者*/
    private String stylesheet3 = "https://cdn.jsdelivr.net/npm/@fancyapps/ui/dist/fancybox.css";
    /**作者*/
    private String stylesheet4 = "/butterfly/css/my.css";
    /**作者*/
    private String generator = "hexo";

    /**markedJs*/
    private String markedJs = "https://cdn.jsdelivr.net/npm/marked/marked.min.js";
    /**highlightJs*/
    private String highlightJs = "https://cdn.jsdelivr.net/gh/highlightjs/cdn-release@11.5.0/build/highlight.min.js";

    private String highlightLineJs =  "https://cdn.jsdelivr.net/npm/highlightjs-line-numbers.js@2.8.0/src/highlightjs-line-numbers.min.js";

    private String highlightCss =  "https://cdn.jsdelivr.net/npm/highlight.js@11.5.0/styles/github-dark-dimmed.css";


    private Map<String,Object> justifiedGallery;

    private Map<String,Object> configSite;


    /**
     * 左侧栏开关
     */
    private Boolean showAsideContent = Boolean.TRUE;

    /**
     * 左侧栏开关
     */
    private Boolean showStickyLayout = Boolean.TRUE;

    /**
     * 首页分页大小
     */
    private Integer indexPageSize = 15;

    private Integer indexSort = 15;

    /**底部标签*/
    private List<Map> copyrightMapList;
    private String copyrightStr;
    /**备案号*/
    private String icpStr;
    /**默认文章图片*/
    private String articleCoverImage = "https://w.wallhaven.cc/full/gj/wallhaven-gjdj13.jpg";

    /**域名*/
    private String host = "http://opadmin.pingyuanren.top";


    /**音乐*/
    private Map playerListMap;







    /**
     *
     */
    @PostConstruct
    public void init(){
        if(justifiedGallery==null){
            justifiedGallery = new HashMap<>();
            justifiedGallery.put("js","'https://cdn.jsdelivr.net/npm/flickr-justified-gallery@2/dist/fjGallery.min.js'");
            justifiedGallery.put("css","'https://cdn.jsdelivr.net/npm/flickr-justified-gallery@2/dist/fjGallery.min.css'");
        }
        if(configSite==null){
            configSite = new HashMap<>();
            configSite.put("title","平原人的博客");
            configSite.put("isPost",false);
            configSite.put("isHome",false);
            configSite.put("isHighlightShrink",false);
            configSite.put("isToc",false);
            configSite.put("postUpdate","2022-03-28 03:44:06");
        }
        if(CollectionUtils.isEmpty(copyrightMapList)){
            copyrightMapList = JSON.parseArray(COPYRIGHT_MAP_LIST_STRING,Map.class);
        }
        if(icpStr==null){
            icpStr = "鄂ICP备2022003082号";
        }
        if(copyrightStr==null){
            copyrightStr = "©2020 - 2022 By 平原人";
        }
        if(playerListMap==null){
            playerListMap = JSON.parseObject(PLAYER_LIST_MAP_STRING,Map.class);
        }
    }

    @Override
    public BlogProperties clone(){
        try {
            BlogProperties clone = (BlogProperties)super.clone();
            clone.justifiedGallery = new HashMap<>(justifiedGallery);
            clone.configSite = new HashMap<>(configSite);
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }













    private static String COPYRIGHT_MAP_LIST_STRING = "[{\n" +
            "\t\"href\": \"https://hexo.io/\",\n" +
            "\t\"src\": \"/svg/Frame-Hexo-blue.svg?style=flat&logo=hexo\",\n" +
            "\t\"dataLazySrc\": \"/svg/Frame-Hexo-blue.svg?style=flat&logo=hexo\",\n" +
            "\t\"title\": \"博客框架为 Hexo\",\n" +
            "\t\"alt\": \"HEXO\"\n" +
            "}, {\n" +
            "\t\"href\": \"https://github.com/jerryc127/hexo-theme-butterfly\",\n" +
            "\t\"src\": \"/svg/Theme-Butterfly.svg?style=flat&logo=bitdefender\",\n" +
            "\t\"dataLazySrc\": \"/svg/Theme-Butterfly.svg?style=flat&logo=bitdefender\",\n" +
            "\t\"title\": \"主题采用 Butterfly\",\n" +
            "\t\"alt\": \"Butterfly\"\n" +
            "}, {\n" +
            "\t\"href\": \"https://www.jsdelivr.com/\",\n" +
            "\t\"src\": \"/svg/CDN-jsDelivr.svg?style=flat&logo=jsDelivr\",\n" +
            "\t\"dataLazySrc\": \"/svg/CDN-jsDelivr.svg?style=flat&logo=jsDelivr\",\n" +
            "\t\"title\": \"本站使用 Jsdelivr 为静态资源提供CDN加速\",\n" +
            "\t\"alt\": \"Jsdelivr\"\n" +
            "}, {\n" +
            "\t\"href\": \"https://github.com/authorZhao/blog-backend\",\n" +
            "\t\"src\": \"/svg/Source-Github.svg?style=flat&logo=GitHub\",\n" +
            "\t\"dataLazySrc\": \"/svg/Source-Github.svg?style=flat&logo=GitHub\",\n" +
            "\t\"title\": \"本站项目由 GitHub 托管\",\n" +
            "\t\"alt\": \"GitHub\"\n" +
            "}, {\n" +
            "\t\"href\": \"http://creativecommons.org/licenses/by-nc-sa/4.0/\",\n" +
            "\t\"src\": \"/svg/Copyright.svg?style=flat&logo=Claris\",\n" +
            "\t\"dataLazySrc\": \"/svg/Copyright.svg?style=flat&logo=Claris\",\n" +
            "\t\"title\": \"本站采用知识共享署名-非商业性使用-相同方式共享4.0国际许可协议进行许可\",\n" +
            "\t\"alt\": \"img\"\n" +
            "}]";


    private static String PLAYER_LIST_MAP_STRING = "{\"dataId\":\"7334007765\",\"dataServer\":\"netease\",\"dataType\":\"playlist\",\"dataFixed\":true,\"dataAutoplay\":true,\"dataVolume\":\"0.3\"}";

}
