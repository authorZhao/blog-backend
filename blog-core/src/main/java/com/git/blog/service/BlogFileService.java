package com.git.blog.service;

import com.git.blog.dto.model.entity.BlogFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author authorZhao
 */
public interface BlogFileService {
    /**
     * 获取当前系统的路径前缀
     * @return
     */
    String getFilePrePath();

    /**
     * 获取中间的路径
     * @return
     */
    String getMidPath();

    String upLoadFile(MultipartFile upLoadFile, String fileName);

}
