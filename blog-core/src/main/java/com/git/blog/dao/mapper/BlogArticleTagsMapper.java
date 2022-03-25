package com.git.blog.dao.mapper;

import com.git.blog.dto.blog.BlogArticleTagsDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;
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


    @Select("<script> SELECT distinct a.id as articleId ,b.tag_id as tagId,c.name  from blog_article a left  join blog_article_tags b on a.id=b.tag_id\n" +
            "\tinner join blog_tag c on b.tag_id=c.id where a.id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>"+
            "#{item}" +
            "</foreach>"+
            "order by articleId asc </script> ")
    List<BlogArticleTagsDTO> getTagsByArticleIds(@Param("ids") List<Long> ids);


    /**
     * 查询标签以及标签文章数量
     * @return
     */
    @Select("select d.id,d.name,count(id) as count from (select distinct a.id,a.name ,c.title  from blog_tag a left join blog_article_tags b on a.id=b.tag_id inner join \n" +
            "blog_article c on b.article_id=c.id) d \n" +
            "group by d.id order by d.id asc")
    List<TagTypeCountDTO> getTagsCount();
}
