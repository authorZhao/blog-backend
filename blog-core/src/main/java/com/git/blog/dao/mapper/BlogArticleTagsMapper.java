package com.git.blog.dao.mapper;

import com.git.blog.dto.model.entity.BlogArticleTags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章标签表 Mapper 接口
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Mapper
public interface BlogArticleTagsMapper extends BaseMapper<BlogArticleTags> {

}
