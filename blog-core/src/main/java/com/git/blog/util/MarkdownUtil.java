package com.git.blog.util;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.List;

/**
 * @author authorZhao
 * @since 2025-04-17
 */
public class MarkdownUtil {

    public static final MutableDataSet OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS, List.of(
                    TablesExtension.create(),
                    TaskListExtension.create(),
                    StrikethroughExtension.create()
            ));

    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder(OPTIONS).build();
        HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS).build();
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
