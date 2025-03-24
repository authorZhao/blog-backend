package com.git.service;

import java.util.regex.Pattern;

public class MarkdownImageReplacer {

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

    public static void main(String[] args) {
        // 示例用法
        String originalMarkdown =
                "![image.png](http://opadmin.pingyuanren.top/file/png/2024/841614b5e3b149a1a8febbc3b8aafb40.png)\n" +
                        "![logo](https://opadmin.pingyuanren.top/images/logo.png)";

        String newDomain = "new.example.com";

        String updated = replaceImageDomain(originalMarkdown, newDomain);
        System.out.println(updated);


        String originalHtml =
                "<img src=\"http://opadmin.pingyuanren.top/file/png/1.png\">\n" +
                        "<img src=\\\"https://opadmin.pingyuanren.top/images/logo.jpg\\\">\n" +
                        "<IMG SRC='http://opadmin.pingyuanren.top/avatar.png'>";

        String updated2 = replaceHtmlImageDomain(originalHtml, newDomain);

        System.out.println("替换结果：\n" + updated2);
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
}
