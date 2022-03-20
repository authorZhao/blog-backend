package com.git.service;

import com.git.blog.dao.service.BlogTagDaoService;
import com.git.blog.dao.service.BlogTypeDaoService;
import com.git.blog.dto.blog.BlogArticleTagsDTO;
import com.git.blog.dto.blog.BlogArticleTypesDTO;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import com.git.blog.BlogWebApplication;

import java.util.List;

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
}
