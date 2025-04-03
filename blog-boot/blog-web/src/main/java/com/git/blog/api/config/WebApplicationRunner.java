package com.git.blog.api.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author authorZhao
 * @since 2020-12-29
 */
@Slf4j
@Configuration
public class WebApplicationRunner implements CommandLineRunner {

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private Environment env;

    @Override
    public void run(String... args) throws Exception {
        log.info("项目启动完成");
        //IpUtil.init("city.free.ipdb");
        //初始化sql
        log.info("-------------------------初始化sql------------------------");
        //initSql();
        log.info("----------sql初始化完成,开始初始化微服务-----------------------");
        //initMsf();
        log.info("-------------------------sql初始化完成,开始初始化微服务---------");


        // start file monitor
        startFileMonitor(args);

    }

    private void initMsf() {
    }

    private void initSql() throws IOException {
        List<String> sql = FileUtils.readLines(new ClassPathResource("init.sql").getFile(), StandardCharsets.UTF_8);
        sql.stream().filter(i->!i.startsWith("-") && StringUtils.isNotBlank(i)).forEach(System.out::println);
    }





    private void startFileMonitor(String... args) throws Exception {

        String s = Arrays.stream(args).filter(i -> i.contains("--spring.config.location=")).findFirst().orElse(null);
        if(StringUtils.isBlank(s)) {
            return;
        }
        var path = s.split("=")[1];
        List<File> files = new ArrayList<>();
//        if(path.endsWith("/")){
//            files.add(new File(URI.create(path + "application.yml").toURL().getFile()));
//            files.add(new File(URI.create(path + "application-prod.yml").toURL().getFile()));
//        }else {
//
//        }

        files.add(new File(URI.create(path).toURL().getFile()));
        files = files.stream().filter(File::exists).filter(File::isDirectory).toList();
        if(files.isEmpty()) {
            return;
        }

        var listener = new FileAlterationListenerAdaptor() {
            @Override
            public void onFileChange(File file) {
                log.info("配置刷新file={}", file);
                if(!file.getName().endsWith(".yml")) {
                    return;
                }
                // 触发配置刷新
                MutablePropertySources propertySources = context.getEnvironment().getPropertySources();


                //env.get
                context.publishEvent(new EnvironmentChangeEvent(context, Set.of()));
                RefreshEndpoint refreshEndpoint = context.getBean(RefreshEndpoint.class);
                refreshEndpoint.refresh();
            }
        };



        log.info("启动configMap文件监听...");
        // configMap挂载路径mountPath
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000);
        files.stream().map(FileAlterationObserver::new).peek(i->i.addListener(listener)).forEach(monitor::addObserver);
        monitor.start();
        log.info("configMap文件监听开始...");
    }



}
