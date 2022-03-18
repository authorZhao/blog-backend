package com.git.blog.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.git.blog.dto.model.entity.BlogFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author authorZhao
 */
public interface BlogFileDaoService extends IService<BlogFile> {

    /**
     * 根据hashId查询
     * @param hashId
     * @return
     */
    BlogFile getByHashId(String hashId);
}
