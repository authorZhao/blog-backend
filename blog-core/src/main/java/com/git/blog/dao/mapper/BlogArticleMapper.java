package com.git.blog.dao.mapper;

import com.git.blog.dto.model.entity.BlogArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Mapper
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {

}
