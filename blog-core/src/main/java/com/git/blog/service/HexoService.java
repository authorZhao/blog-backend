package com.git.blog.service;

import com.git.blog.dto.model.entity.BlogArticle;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.util.List;

/**
 * HexoService配置
 * @author authorZhao
 * @since 2022-03-15
 */
public interface HexoService {

    /**
     * 生成静态博客文件
     * @param blogArticle 博客本体
     * @param typeIds 类型ids
     * @param tagIds 标签ids
     * @param uid 用户id
     */
    @Async
    void generateHtml(BlogArticle blogArticle,List<Long> typeIds,List<Long> tagIds,Long uid);

    /**
     * 生成md文件
     * @param blogArticle
     * @param typeIds
     * @param tagIds
     * @param uid
     */
    File generateMd(BlogArticle blogArticle, List<Long> typeIds, List<Long> tagIds, Long uid);

    /**
     * 执行命令
     */
    void exeHexoCleanGenerate();

    void deleteAllMd();
}
