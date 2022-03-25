package com.git.blog.dao.mapper;

import com.git.blog.dto.blog.BlogArticleTypesDTO;
import com.git.blog.dto.blog.TagTypeCountDTO;
import com.git.blog.dto.model.entity.BlogArticleTypes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 文章类型表 Mapper 接口
 * </p>
 *
 * @author authorZhao
 * @since 2022-03-05
 */
@Mapper
public interface BlogArticleTypesMapper extends BaseMapper<BlogArticleTypes> {

    @Select("<script> SELECT distinct a.id as articleId ,b.type_id as typeId,c.name  from blog_article a left  join blog_article_types b on a.id=b.article_id\n" +
            "\tinner join blog_type c on b.type_id=c.id where a.id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>"+
            "#{item}" +
            "</foreach>"+
            "order by articleId asc </script>")
    List<BlogArticleTypesDTO> getTypesByArticleIds(@Param("ids") List<Long> ids);


    /**
     * 查询某一个类型有多少
     * @return
     */
    @Select("select d.id,d.name,count(id) as count from (select distinct a.id,a.name ,c.title  from blog_type a left join blog_article_types b on a.id=b.type_id inner join \n" +
            "blog_article c on b.article_id=c.id where a.status=0) d \n" +
            "group by d.id order by d.id asc")
    List<TagTypeCountDTO> getTypesCount();
}
