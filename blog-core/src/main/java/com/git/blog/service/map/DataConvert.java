package com.git.blog.service.map;

import com.alibaba.fastjson2.JSON;
import com.git.blog.dto.blog.*;
import com.git.blog.dto.config.ConfigDetailDTO;
import com.git.blog.dto.config.ConfigPageDTO;
import com.git.blog.dto.config.HistoryConfigItem;
import com.git.blog.dto.menu.MenuTreeVO;
import com.git.blog.dto.menu.MenuVO;
import com.git.blog.dto.model.entity.*;
import com.git.blog.dto.role.RoleVO;
import com.git.blog.dto.user.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DataConvert {

    DataConvert INSTANCE = Mappers.getMapper(DataConvert.class);

    /**
     * DTO->entity
     * @param entity
     * @return
     */
    @Mapping(target = "historyList", source = "history",qualifiedByName = "toItemList")
    ConfigDetailDTO entityToDetailDTO(ConfigEntity entity);
    ConfigPageDTO entityToPageDTO(ConfigEntity entity);



    @Named("toItemList")
    static List<HistoryConfigItem> toItemList(String person) {
        return JSON.parseArray(person, HistoryConfigItem.class);
    }

    BlogTagDTO tagToTagDTO(BlogTag blogTagDTO);
    BlogTag tagDTOToTag(BlogTagDTO blogTag);
    BlogTypeDTO typeToTypesDTO(BlogType blogTypeDTO);
    BlogType typeDTOToType(BlogTypeDTO blogType);
    BlogArticleVO articleToArticleVO(BlogArticle blogArticle);
    BlogArticleDetailVO articleToArticleDetailVO(BlogArticle blogArticle);
    BlogArticle articleDTOToArticle(BlogArticleDTO blogArticleDTO);
    BlogArticleDTO articleToArticleDTO(BlogArticle blogArticle);

    /**
     * 菜单实体和菜单返回对象之间转化
     * @param menu
     * @return
     */
    MenuVO convertMenuToVO(Menu menu);

    MenuTreeVO convertMenuToMenuTreeVO(Menu menu);

    User convertUserDTOToUser(UserDTO userDTO);

    /**
     * 角色实体转化
     * @param role
     * @return
     */
    RoleVO convertRoleToRoleVO(Role role);
}
