package com.git.blog.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.git.blog.commmon.PageParam;

/**
 * @author authorZhao
 * @since 2020-12-25
 */
public class PageUtil {

    public static Page convert(PageParam pageParam){
        return new Page(pageParam.getCurrent(),pageParam.getPageSize());
    }
}
