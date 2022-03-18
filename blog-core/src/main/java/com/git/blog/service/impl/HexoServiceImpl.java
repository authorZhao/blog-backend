package com.git.blog.service.impl;

import com.git.blog.config.properties.SysProperties;
import com.git.blog.dao.service.BlogArticleDaoService;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.model.entity.BlogArticle;
import com.git.blog.dto.model.entity.BlogTag;
import com.git.blog.dto.model.entity.BlogType;
import com.git.blog.dto.model.entity.User;
import com.git.blog.service.HexoService;
import com.git.blog.service.TagTypeService;
import com.git.blog.service.UserService;
import com.git.blog.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2022-03-15
 */
@Service
@Slf4j
public class HexoServiceImpl implements HexoService {

    @Autowired
    private SysProperties sysProperties;
    @Autowired
    private UserService userService;
    @Autowired
    private BlogArticleDaoService blogArticleDaoService;

    @Autowired
    private BlogTagDaoService blogTagDaoService;
    @Autowired
    private BlogTypeDaoService blogTypeDaoService;

    @Override
    public void generateHtml(BlogArticle blogArticle, List<Long> typeIds, List<Long> tagIds, Long uid) {
        generateMd(blogArticle,typeIds,tagIds,uid);
        exeHexoCleanGenerate();
    }

    @Override
    public void generateMd(BlogArticle blogArticle, List<Long> typeIds, List<Long> tagIds, Long uid) {
        if(BooleanUtils.isFalse(sysProperties.getHexoUse()))return;

        //数据准备
        User userById = userService.getUserById(uid);
        if(userById==null)return;
        BlogArticle dbArticle = blogArticleDaoService.getById(blogArticle.getId());
        String types = CollectionUtils.isEmpty(typeIds)?"categories: ": "categories: "+blogTypeDaoService.listByIds(typeIds).stream().sorted(Comparator.comparingInt(BlogType::getSort))
                .map(BlogType::getName)
                .distinct()
                .collect(Collectors.joining(","));

        String tags = CollectionUtils.isEmpty(tagIds)?"tags: ":"tags: "+blogTagDaoService.listByIds(typeIds).stream().sorted(Comparator.comparingLong(BlogTag::getId))
                .map(BlogTag::getName).distinct().collect(Collectors.joining(","));
        String contentMd = blogArticle.getContentMd();
        String cover = blogArticle.getCoverImage();
        String updateTime = LocalDateTime.now().format(TimeUtils.DATETIME_FORMATTER);
        String title = blogArticle.getTitle();
        String description = blogArticle.getDescription();
        String createTime = Optional.ofNullable(dbArticle).map(BlogArticle::getCreateTime).map(i->i.format(TimeUtils.DATETIME_FORMATTER)).orElse(updateTime);
        String frontMatter = String.format(TEMPLATE,title,createTime,updateTime,types,tags,description,cover);
        String content = frontMatter+contentMd;
        try {
            FileUtils.writeStringToFile(new File(sysProperties.getHexoPath()+"/source/_posts/"+ title +".md"), content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllMd() {
        File file = new File(sysProperties.getHexoPath() + "/source/_posts/");
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            log.error("deleteAllMd error",e);
        }
    }

    @Override
    public void exeHexoCleanGenerate() {
        String systemName = System.getProperty("os.name").toLowerCase();
        String result = "";
        if (systemName.startsWith("win")) {
            result = executeCommand(new String[]{"cmd.exe", "/C", "echo %cd% & hexo clean & hexo generate"}, sysProperties.getHexoPath());
        } else {
            result = executeCommand(new String[]{"/bin/sh", "-c", "pwd && hexo clean && hexo generate"}, sysProperties.getHexoPath());
        }
        log.info("generateHtml result={}", result);
    }



    /**
     * 执行命令
     * @param mdCommand 命令
     * @param dir 执行目录
     * @return
     */
    public static String executeCommand(String[] mdCommand,String dir) {
        StringBuilder stringBuilder = new StringBuilder();
        Process process = null;
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec(mdCommand,null,new File(dir));
            bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8.name()));
            String line = null;
            while((line=bufferedReader.readLine()) != null){
                stringBuilder.append(line+"\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            if(bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if(process!=null) {
                process.destroy();
            }
            e.printStackTrace();
            return null;
        }
    }


    public static String TEMPLATE = "---\n" +
            "title: %s\n" +
            "date: %s\n" +
            "updated: %s\n" +
            "comments: true\n" +
            "%s\n" +
            "%s\n" +
            "description: %s\n" +
            "cover: %s\n" +
            "---\n\n";
}
