package com.git.blog.service;

import com.git.blog.dto.blog.BlogTagDTO;
import com.git.blog.dto.blog.BlogTypeDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;
import com.git.blog.dto.blog.TagTypeDetailDTO;

import java.util.List;

/**
 * @author authorZhao
 * @since 2022-03-06
 */
public interface TagTypeService {
    /**
     * 增加标签
     * @param blogTagDTO
     * @return
     */
    Boolean addTag(BlogTagDTO blogTagDTO);

    /**
     * 修改标签
     * @param blogTagDTO
     * @return
     */
    Boolean updateTag(BlogTagDTO blogTagDTO);

    /**
     * 删除标签
     * @param id
     * @return
     */
    Boolean deleteTag(Long id);

    /**
     * 批量删除标签
     * @param ids
     * @return
     */
    Boolean deleteTags(List<Long> ids);

    /**
     * 获取所有标签
     * @return
     */
    List<BlogTagDTO> queryAllTags();

    /**
     * 增加类文章类型
     * @param blogTagDTO
     * @return
     */
    Boolean addType(BlogTypeDTO blogTagDTO);

    /**
     * 修改文章类型
     * @param blogTagDTO
     * @return
     */
    Boolean updateType(BlogTypeDTO blogTagDTO);

    /**
     * 删除文章类型
     * @param id
     * @return
     */
    Boolean deleteType(Long id);

    /**
     * 批量删除文章类型
     * @param ids
     * @return
     */
    Boolean deleteTypes(List<Long> ids);

    /**
     * 查询所有的类型不返回删除状态
     * @return
     */
    List<BlogTypeDTO> queryAllTypes();

    /**
     * 查询所有的类型返回所有状态的
     * @return
     */
    List<BlogTypeDTO> listTypes();

    /**
     * 查询所有的类型，返回树形结构
     * @return
     */
    List<BlogTypeDTO> treeTypes();

    /**
     * 获取标签详情
     * @param tagId
     * @return
     */
    TagTypeDetailDTO getTagDetail(Long tagId);

    /**
     * 获取分类详情
     * * @param typeId
     * @return
     */
    TagTypeDetailDTO getTypeDetail(Long typeId);

    /**
     * 获取系统的tag和type
     * @param key
     * @return
     */
    List<TagTypeCountDTO> getTagType(String key);

}
