package com.git.blog.dao.mapper;

import com.git.blog.dto.blog.BlogArticleTypesDTO;
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

    @Select("<script> select distinct a.id,a.name,b.article_id as articleId from \n" +
            "blog_type a inner join blog_article_types b ON a.id=b.type_id Inner join blog_article c\n" +
            "on b.article_id=c.id where a.status=0 and c.id in" +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>"+
            "#{item}" +
            "</foreach>"+
            "order by articleId asc </script>")
    List<BlogArticleTypesDTO> getTypesByArticleIds(@Param("ids") List<Long> ids);
}
