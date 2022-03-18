package com.git.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.properties.FileProperties;
import com.git.blog.config.properties.SysProperties;
import com.git.blog.dao.service.BlogFileDaoService;
import com.git.blog.dto.model.entity.BlogFile;
import com.git.blog.exception.ErrorException;
import com.git.blog.service.BlogFileService;
import com.git.blog.util.BlogFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * @author authorZhao
 * @since 2022-03-03
 */
@Service
@Slf4j
public class BlogFileServiceImpl implements BlogFileService {
    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private BlogFileDaoService blogFileDaoService;
    @Autowired
    private SysProperties sysProperties;

    @Override
    public String getFilePrePath() {
        String systemName = System.getProperty("os.name").toLowerCase();
        if(systemName.startsWith("win")){
            return fileProperties.getWindowUpLoadPathPrefix();
        }else{
            return fileProperties.getLinuxUpLoadPathPrefix();
        }
    }

    @Override
    public String getMidPath() {
        return fileProperties.getSufPath();
    }

    @Override
    public String upLoadFile(MultipartFile upLoadFile, String fileName) {
        String hashId = null;
        try {
            hashId = BlogFileUtil.md5HashCode32(upLoadFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BlogFile dbFile= blogFileDaoService.getByHashId(hashId);
        if(dbFile!=null){
            log.info("文件上传重复:fileName={},dbFile={}",fileName, JSON.toJSONString(fileName));
            return sysProperties.getImgHost()+dbFile.getPath();
        }

        File file = null;
        BlogFile oFile = null;
        try {
            oFile = getBlogFile(upLoadFile, fileName);
            file = getUpLoadFile(oFile.getPath());
            oFile.setHashId(hashId);
            upLoadFile.transferTo(file);
            blogFileDaoService.save(oFile);
        } catch (IOException e) {
            if(file!=null)file.delete();
            log.error("文件上传错误:fileName={}",file,e);
        }

        return Optional.ofNullable(oFile).map(BlogFile::getPath).map(i->sysProperties.getImgHost()+i).orElse(null);
    }


    /**
     *
     * @param upLoadFile
     * @param fileName
     * @return
     */
    private BlogFile getBlogFile(MultipartFile upLoadFile, String fileName) {
        BlogFile oFile = new BlogFile();
        if(upLoadFile.getSize()>fileProperties.getFileSize())throw new ErrorException("file is so big");
        String name = upLoadFile.getOriginalFilename();
        //TODO 暂时简单根据后缀名来区分，后面改为
        if(name.contains("exe")||name.contains("bat"))throw new ErrorException("this is not file");
        String fileType = BlogFileUtil.getFileType(name);
        oFile.setFileType(fileType);
        if(StringUtils.isBlank(fileName)){
            oFile.setName(BlogFileUtil.getFileName(name));
        }else {
            oFile.setName(fileName);
        }
        oFile.setSize(upLoadFile.getSize());
        oFile.setUserId(AuthTheadLocal.get());
        /**
         * 上传文件策略时间+uuid
         */
        String date = new SimpleDateFormat("yyyy").format(new Date()).toString().toLowerCase();
        String path = fileProperties.getSufPath()+fileType+"/"+date+"/"+ UUID.randomUUID().toString().replaceAll("-","")+"."+fileType;
        oFile.setPath(path);
        return oFile;
    }

    /**
     * 获取文件资源
     * @param path 资源路径
     * 上传：上传的路径   	  xml/2022/97414fb8646746f1b5ad241aaf6ed351.xml
     * 下载：下载的参数为 file/2022/97414fb8646746f1b5ad241aaf6ed351.xml
     *
     * @return file
     */
    private File getUpLoadFile(String path) {
        String systemName = System.getProperty("os.name").toLowerCase();
        String filePath = "";
        if(systemName.startsWith("win")){
            filePath = fileProperties.getWindowUpLoadPathPrefix();
        }else{
            filePath =  fileProperties.getLinuxUpLoadPathPrefix();
        }
        return BlogFileUtil.getFile(filePath+path);
    }
}
