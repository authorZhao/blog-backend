package com.git.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author authorZhao
 * @since 2020-12-28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "head")
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
}
