package com.git.blog.dao.mapper;

import com.git.blog.dto.blog.BlogArticleTagsDTO;
import com.git.blog.dto.model.entity.BlogArticleTags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.git.blog.dto.model.entity.BlogTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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


    @Select("<script> select distinct a.id,a.name,b.article_id as articleId from \n" +
            "blog_tag a inner join blog_article_tags b ON a.id=b.tag_id Inner join blog_article c\n" +
            "on b.article_id=c.id where c.id in" +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>"+
            "#{item}" +
            "</foreach>"+
            "order by articleId asc </script> ")
    List<BlogArticleTagsDTO> getTagsByArticleIds(@Param("ids") List<Long> ids);
}
