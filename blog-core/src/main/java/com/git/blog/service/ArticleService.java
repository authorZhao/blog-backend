package com.git.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.dto.blog.BlogArticleDTO;
import com.git.blog.dto.blog.BlogArticleDetailVO;
import com.git.blog.dto.blog.BlogArticlePageDTO;
import com.git.blog.dto.blog.BlogArticleVO;

/**
 * @author authorZhao
 * @since 2022-03-06
 */
public interface ArticleService {
    /**
     * 分页
     * @param blogArticlePageDTO
     * @return
     */
    Page<BlogArticleVO> pageArticle(BlogArticlePageDTO blogArticlePageDTO);

    /**
     * 新增和修改
     * @param blogArticlePageDTO
     * @return
     */
    Boolean saveOrUpdate(BlogArticleDTO blogArticlePageDTO);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean deleteArticle(Long id);

    /**
     * 恢复文章
     * @param id
     * @return
     */
    Boolean recoveryArticle(Long id);

    /**
     * 查看详情
     * @param id
     * @return
     */
    BlogArticleDetailVO detailArticle(Long id);

    /**
     * 重新初始化博客
     * @return
     */
    Boolean init();

}
