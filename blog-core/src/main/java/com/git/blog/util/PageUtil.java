package com.git.blog.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.git.blog.commmon.PageParam;
import org.apache.commons.collections4.CollectionUtils;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
public class PageUtil {

    public static Page convert(PageParam pageParam) {
        return new Page(pageParam.getCurrent(), pageParam.getPageSize());
    }

    @SuppressWarnings({"unchecked"})
    public static <T,R> Page<R> convert(Page<T> page, Function<T,R> function) {
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return (Page<R>) page;
        }
        var list = page.getRecords().stream().map(function).collect(Collectors.toList());
        Page<R> newPage = (Page<R>) page;
        newPage.setRecords(list);
        return newPage;
    }

    public static String replaceImageDomain(String markdownContent, String newDomain) {
        // 定义正则表达式（注意 Java 的字符串转义）
        String regex = "(\\!\\[[^\\]]*\\]\\()(https?://)opadmin\\.pingyuanren\\.top(\\/[^)]*\\))";

        // 构建替换模板（保留原协议 + 新域名）
        String replacement = "$1$2" + newDomain + "$3";

        // 执行正则替换
        return Pattern.compile(regex)
                .matcher(markdownContent)
                .replaceAll(replacement);
    }


    public static String replaceHtmlImageDomain(String htmlContent, String newDomain) {
        // 匹配含转义符的 img 标签正则
        String regex =
                "(<img\\s+src=\\\\?[\"'])" +  // 第1组：匹配 img src 开头（支持 src=\" 和 src="）
                        "(https?://)" +              // 第2组：协议
                        "opadmin\\.pingyuanren\\.top" + // 原域名
                        "([^\"']*+)" +               // 第3组：路径部分（非贪婪匹配到引号前）
                        "(\\\\?[\"'])";             // 第4组：闭合引号（支持 \" 和 "）

        // 替换保留原始引号风格
        String replacement = "$1$2" + newDomain + "$3$4";

        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
                .matcher(htmlContent)
                .replaceAll(replacement);
    }

    public static String replaceImg(String url, String content) {
        if (url.endsWith("/")) {
            url = url.substring(7, url.length() - 1);
        } else {
            url = url.substring(7);
        }
        return replaceHtmlImageDomain(content, url);
    }
}
