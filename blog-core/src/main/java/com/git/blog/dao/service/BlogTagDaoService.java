package com.git.blog.dao.service;

import com.git.blog.dto.blog.BlogArticleTagsDTO;
import com.git.blog.dto.blog.BlogTagDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;
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
     * @param uid
     * @return
     */
    List<BlogTag> listByUid(Long uid);

    /**
     * 根据uid查询数量
     * @param uid
     * @return
     */
    Integer countByUid(Long uid);

    List<Long> getTagIdsByArticleId(Long articleIds);

    List<BlogTag> getTagsByArticleId(Long articleIds);

    /**
     *
     * @param articleIds
     * @return
     */
    List<BlogArticleTagsDTO> getTagsByArticleIds(List<Long> articleIds);

    /**
     * 根据tagId删除中间表数据
     * @param tagId
     */
    void deleteArticleTag(Long tagId);

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

    /**
     * 获取标签文章数量
     * @return
     */
    List<TagTypeCountDTO> getTagsCount();

    /**
     * 根据tagId获取文章ids
     * @param tagId
     * @return
     */
    List<Long> getArticleIds(Long tagId);
}
