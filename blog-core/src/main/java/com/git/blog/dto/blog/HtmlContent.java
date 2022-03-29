package com.git.blog.dto.blog;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author authorZhao
 * @since 2022-03-30
 */
@Data
@Accessors(chain = true)
public class HtmlContent {
    /**标题*/
    private String tocHtml;

    /**文章内容*/
    private String articleHtml;
}
