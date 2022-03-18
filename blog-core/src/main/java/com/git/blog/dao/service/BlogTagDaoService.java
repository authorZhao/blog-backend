package com.git.blog.dao.service;

import com.git.blog.dto.blog.BlogTagDTO;
import com.git.blog.dto.model.entity.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 标签表，作为二级分类直接与uid关联 服务类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
public interface BlogTagDaoService extends IService<BlogTag> {

    /**
     * 根据uid查询所有
     * @param aLong
     * @return
     */
    List<BlogTag> listByUid(Long aLong);

    /**
     * 根据uid查询数量
     * @param aLong
     * @return
     */
    Integer countByUid(Long aLong);

    List<Long> getTagIdsByArticleId(Long id);

    List<BlogTag> getTagsByArticleId(Long id);

    /**
     * 根据tagId删除中间表数据
     * @param id
     */
    void deleteArticleTag(Long id);

    /**
     * tagId过滤
     * @param tagIds
     * @return
     */
    List<Long> filterTagIds(List<Long> tagIds,Long uid);

    /**
     * 新增中间表数据
     * @param tagIds 标签id
     * @param id 文章id
     */
    void saveByTagIdsAndArticleId(List<Long> tagIds, Long id);
}
