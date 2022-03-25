package com.git.blog.service.bean;

import com.git.blog.dto.blog.*;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.menu.MenuVO;
import com.git.blog.dto.model.entity.*;
import com.git.blog.dto.role.RoleVO;
import com.git.blog.dto.user.UserDTO;

/**
 * 用户角色菜单之间的bean转化
 * @author authorZhao
 * @since 2020-12-25
 */
public interface BlogMapper {
    BlogTagDTO tagToTagDTO(BlogTag blogTagDTO);
    BlogTag tagDTOToTag(BlogTagDTO blogTag);
    BlogTypeDTO typeToTypesDTO(BlogType blogTypeDTO);
    BlogType typeDTOToType(BlogTypeDTO blogType);
    BlogArticleVO articleToArticleVO(BlogArticle blogArticle);
    BlogArticleDetailVO articleToArticleDetailVO(BlogArticle blogArticle);
    BlogArticle articleDTOToArticle(BlogArticleDTO blogArticleDTO);
    BlogArticleDTO articleToArticleDTO(BlogArticle blogArticle);

}
