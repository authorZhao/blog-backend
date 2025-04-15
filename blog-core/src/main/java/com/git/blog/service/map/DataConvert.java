package com.git.blog.service.map;

import com.alibaba.fastjson2.JSON;
import com.git.blog.dto.config.ConfigDetailDTO;
import com.git.blog.dto.config.ConfigPageDTO;
import com.git.blog.dto.config.HistoryConfigItem;
import com.git.blog.dto.model.entity.ConfigEntity;
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
}
