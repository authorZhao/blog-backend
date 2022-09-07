package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.CommonString;
import com.git.blog.commmon.PageParam;
import com.git.blog.dao.service.ShiCiTypeDaoService;
import com.git.blog.dao.service.impl.ShiCiSongCiDaoServiceImpl;
import com.git.blog.dto.blog.TagTypeCountDTO;
import com.git.blog.dto.model.entity.BlogArticle;
import com.git.blog.dto.shici.ShiCiSongCiDTO;
import com.git.blog.entity.ShiCiSongCi;
import com.git.blog.entity.ShiCiType;
import com.git.blog.service.CacheService;
import com.git.blog.service.ShiCiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2022-09-08
 */
@Service
@Slf4j
public class ShiCiServiceImpl implements ShiCiService {

    @Autowired
    private ShiCiSongCiDaoServiceImpl shiCiSongCiDaoService;

    @Autowired
    private ShiCiTypeDaoService shiCiTypeDaoService;
    @Autowired
    private CacheService cacheService;

    @Override
    public Page pageByType(Long type, PageParam pageParam) {
        Wrapper<ShiCiSongCi> wrapper = new LambdaQueryWrapper<ShiCiSongCi>()
                .eq(ShiCiSongCi::getType,type);
        Page<ShiCiSongCi> page = new Page<>(pageParam.getCurrent(), pageParam.getPageSize());
        page = shiCiSongCiDaoService.page(page, wrapper);

        List collect = page.getRecords().stream().map(i -> {
            ShiCiSongCiDTO shiCiSongCiDTO = new ShiCiSongCiDTO();
            BeanUtils.copyProperties(i, shiCiSongCiDTO);

            String paragraphs = shiCiSongCiDTO.getParagraphs();
            List<String> stringList = JSON.parseArray(paragraphs,String.class);
            if(CollectionUtils.isNotEmpty(stringList)){
                String s = stringList.get(0);
                String[] split = s.split("，");
                shiCiSongCiDTO.setParagraphs(split[0]);
            }
            return shiCiSongCiDTO;
        }).collect(Collectors.toList());
        page.setRecords(collect);
        return page;
    }

    @Override
    public List<ShiCiType> listType() {
        List<ShiCiType> tagTypeCountDTOS = Collections.emptyList();
        String str = cacheService.getStr("shi.ci.type");

        //走缓存
        if (StringUtils.isNotEmpty(str)) {
            return JSON.parseArray(str, ShiCiType.class);
        }

        //查数据
        tagTypeCountDTOS = shiCiTypeDaoService.list();
        cacheService.setStr("shi.ci.type", JSON.toJSONString(tagTypeCountDTOS));
        return tagTypeCountDTOS;
    }

    @Override
    public ShiCiSongCiDTO getShiCiById(Long id) {
        ShiCiSongCi byId = shiCiSongCiDaoService.getById(id);
        ShiCiSongCiDTO shiCiSongCiDTO = new ShiCiSongCiDTO();
        BeanUtils.copyProperties(byId, shiCiSongCiDTO);
        String paragraphs = shiCiSongCiDTO.getParagraphs();
        List<String> stringList = JSON.parseArray(paragraphs,String.class);
        shiCiSongCiDTO.setParagraphList(stringList);
        return shiCiSongCiDTO;
    }
}
