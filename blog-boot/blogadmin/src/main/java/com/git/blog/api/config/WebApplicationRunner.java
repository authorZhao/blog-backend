package com.git.blog.api.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author authorZhao
 * @since 2020-12-29
 */
@Slf4j
@Configuration
public class WebApplicationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("项目启动完成");
        //初始化sql
        log.info("-------------------------初始化sql------------------------");
        //initSql();
        log.info("----------sql初始化完成,开始初始化微服务-----------------------");
        //initMsf();
        log.info("-------------------------sql初始化完成,开始初始化微服务---------");

    }

    private void initMsf() {
    }

    private void initSql() throws IOException {
        List<String> sql = FileUtils.readLines(new ClassPathResource("init.sql").getFile(), StandardCharsets.UTF_8);
        sql.stream().filter(i->!i.startsWith("-") && StringUtils.isNotBlank(i)).forEach(System.out::println);
    }
}
