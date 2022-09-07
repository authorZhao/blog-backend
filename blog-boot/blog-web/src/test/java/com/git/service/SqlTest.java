package com.git.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dao.service.ShiCiAuthorService;
import com.git.blog.dao.service.ShiCiSongCiService;
import com.git.blog.dto.blog.BlogArticleTagsDTO;
import com.git.blog.dto.blog.BlogArticleTypesDTO;
import com.git.blog.entity.ShiCiAuthor;
import com.git.blog.entity.ShiCiSongCi;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import com.git.blog.BlogWebApplication;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @since 2022-03-20
 */
@SpringBootTest(classes = BlogWebApplication.class)
@Profile("dev")
@ActiveProfiles("dev")
public class SqlTest {

    @Autowired
    private BlogTypeDaoService blogTypeDaoService;

    @Autowired
    private BlogTagDaoService blogTagDaoService;
    @Autowired
    private ShiCiAuthorService shiCiAuthorService;
    @Autowired
    private ShiCiSongCiService shiCiSongCiService;

    @Test
    public void test1(){
        List<BlogArticleTypesDTO> typesByArticleIds = blogTypeDaoService.getTypesByArticleIds(Lists.newArrayList(1L, 2L, 3L));
        System.out.println("typesByArticleIds = " + typesByArticleIds);
    }

    @Test
    public void test2(){
        List<BlogArticleTagsDTO> typesByArticleIds = blogTagDaoService.getTagsByArticleIds(Lists.newArrayList(1L, 2L, 3L));
        System.out.println("typesByArticleIds = " + typesByArticleIds);
    }

    @Test
    public void test3() throws Exception{
        String s = FileUtils.readFileToString(new File("E:\\project\\chinese-poetry\\ci\\author.song.json"), StandardCharsets.UTF_8);
        List<Map<String,String>> map = JSON.parseObject(s, new TypeReference<List<Map<String,String>>>(){});

        List<ShiCiAuthor> collect = map.stream().map(i -> {
            return new ShiCiAuthor().setName(i.get("name"))
                    .setDescription(i.get("description"))
                    .setShortDescription(i.get("short_description"));

        }).collect(Collectors.toList());
        shiCiAuthorService.saveBatch(collect);

    }

    @Test
    public void test4() throws Exception{

        for (File file : new File("E:\\project\\chinese-poetry\\ci").listFiles()) {
            if(file.getName().startsWith("ci.song")){
                String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                List<Map<String,String>> map = JSON.parseObject(s, new TypeReference<List<Map<String,String>>>(){});
                List<ShiCiSongCi> collect = map.stream().map(i -> {
                    return new ShiCiSongCi().setAuthor(i.get("author"))
                            .setParagraphs(i.get("paragraphs"))
                            .setRhythmic(i.get("rhythmic"));

                }).collect(Collectors.toList());
                shiCiSongCiService.saveBatch(collect);
            }
        }



    }
}
