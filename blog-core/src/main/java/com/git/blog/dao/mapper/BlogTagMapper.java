package com.git.blog.dao.mapper;

import com.git.blog.dto.model.entity.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 标签表，作为二级分类直接与uid关联 Mapper 接口
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {

}
