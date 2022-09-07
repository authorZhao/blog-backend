package com.git.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.PageParam;
import com.git.blog.dto.shici.ShiCiSongCiDTO;
import com.git.blog.entity.ShiCiSongCi;
import com.git.blog.entity.ShiCiType;

import java.util.List;

/**
 * @author authorZhao
 * @since 2022-09-08
 */
public interface ShiCiService {

    /**
     * 根据类型分页
     * @param type
     * @param pageParam
     * @return
     */
    Page<ShiCiSongCiDTO> pageByType(Long type, PageParam pageParam);

    /**
     * 获取所有的类型
     * @return
     */
    List<ShiCiType> listType();

    /**
     * 获取所有的类型
     * @return
     */
    default ShiCiType getTypeById(Long id){
        return listType().stream().filter(i->i.getId().equals(id)).findFirst().orElse(null);
    }


    ShiCiSongCiDTO getShiCiById(Long id);
}
