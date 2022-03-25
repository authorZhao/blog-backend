package com.git.blog.dao.service;

import com.git.blog.dto.blog.BlogArticleTypesDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;
import com.git.blog.dto.model.entity.BlogType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章分类表 服务类
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
public interface BlogTypeDaoService extends IService<BlogType> {

    /**
     * 根据状态查询所有
     * @param typeNormalStatus
     * @return
     */
    List<BlogType> listByStatus(Integer typeNormalStatus);

    /**
     * 根据文章id查询类型ids
     * @param id
     * @return
     */
    List<Long> getTypeIdsByArticleId(Long id);

    /**
     * 根据文章id查询类型
     * @param id
     * @return
     */
    List<BlogType> getTypesByArticleId(Long id);

    /**
     * 根据文章id查询类型
     * @param ids
     * @return
     */
    List<BlogArticleTypesDTO> getTypesByArticleIds(List<Long> ids);

    /**
     * 根据文章删除类型
     * @param typeId
     */
    void deleteArticleType(Long typeId);

    /**
     * 根据类型筛选过滤正确的数据
     * @param tagIds
     * @return
     */
    List<Long> filterTypeIds(List<Long> tagIds);

    /**
     * 中间表新增
     * @param typeIds 类型id
     * @param id 文章id
     */
    void saveByTypeIdsAndArticleId(List<Long> typeIds, Long id);

    /**
     * 获取分类以及文章数量
     * @return
     */
    List<TagTypeCountDTO> getTypesCount();

    /**
     * 根据typeId查询articleIds
     * @param typeId
     * @return
     */
    List<Long> getArticleIds(Long typeId);
}
