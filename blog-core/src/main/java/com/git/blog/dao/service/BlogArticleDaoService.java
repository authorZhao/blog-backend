package com.git.blog.dao.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.dto.model.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
public interface BlogArticleDaoService extends IService<BlogArticle> {

    /**
     * 根据ids和状态查询
     * @param ids
     * @param status
     * @return
     */
    default List<BlogArticle> listByIdsAndStatus(List<Long> ids,Integer status){
        return list(new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getId,BlogArticle::getTitle,BlogArticle::getUserId,BlogArticle::getCoverImage,BlogArticle::getEditorType
                        ,BlogArticle::getQrcodePath,BlogArticle::getTop,BlogArticle::getStatus,BlogArticle::getRecommended
                        ,BlogArticle::getOriginal,BlogArticle::getDescription,BlogArticle::getKeywords,BlogArticle::getComment
                        ,BlogArticle::getPassword,BlogArticle::getRequiredAuth,BlogArticle::getReprintUrl,BlogArticle::getCreateTime,BlogArticle::getUpdateTime
                )
                .in(BlogArticle::getId,ids)
                .eq(status!=null,BlogArticle::getStatus,status));
    }

    /**
     * 不查询md和content
     * @param limit
     * @param status
     * @return
     */
    default List<BlogArticle> listLimitAndStatus(Integer limit,Integer status){
        return list(new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getId,BlogArticle::getTitle,BlogArticle::getUserId,BlogArticle::getCoverImage,BlogArticle::getEditorType
                        ,BlogArticle::getQrcodePath,BlogArticle::getTop,BlogArticle::getStatus,BlogArticle::getRecommended
                        ,BlogArticle::getOriginal,BlogArticle::getDescription,BlogArticle::getKeywords,BlogArticle::getComment
                        ,BlogArticle::getPassword,BlogArticle::getRequiredAuth,BlogArticle::getReprintUrl,BlogArticle::getCreateTime,BlogArticle::getUpdateTime
                ).eq(status!=null,BlogArticle::getStatus,status)
                .orderByDesc(BlogArticle::getUpdateTime)
                .last(String.format(CommonString.LAST_SQL_LIMIT,limit)));

    }
}
