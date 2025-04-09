package com.git.blog.api.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    //@Autowired
    private YamlPropertySourceLoader yamlPropertySourceLoader;

    @Override
    public void run(String... args) throws Exception {
        yamlPropertySourceLoader = new YamlPropertySourceLoader();
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
            s = env.getProperty("spring.config.location");
        }
        if(StringUtils.isBlank(s)) {
            return;
        }
        var path = s.split("=")[1];
        List<File> files = new ArrayList<>();
        files.add(new File(URI.create(path).toURL().getFile()));
        files = files.stream().filter(File::exists).map(i->{
            if(i.isDirectory()) {
                return i;
            }else {
                return i.getParentFile();
            }
        }).distinct().toList();
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
                context.publishEvent(new RefreshEvent(this, null, "Refresh Nacos config"));
            }
        };



        log.info("启动configMap文件监听...");
        // configMap挂载路径mountPath
        FileAlterationMonitor monitor = new FileAlterationMonitor(1000);
        files.stream().map(FileAlterationObserver::new).peek(i->i.addListener(listener)).forEach(monitor::addObserver);
        monitor.start();
        log.info("configMap文件监听开始...");
    }

    private <T> T biConsumer(T oldData, T newData) {
        return newData;
    }


    public static Set<String> compareMap(Map<String, Object> oldMap, Map<String, Object> newConfig) {
        Set<String> changedKeys = new HashSet<>();

        // 比较新增或修改的 Key
        newConfig.forEach((key, newValue) -> {
            Object oldValue = oldMap.get(key);
            if (!Objects.equals(newValue, oldValue)) {
                changedKeys.add(key);
            }
        });

        // 检查被删除的 Key
        oldMap.keySet().stream()
                .filter(key -> !newConfig.containsKey(key))
                .forEach(changedKeys::add);

        return changedKeys;
    }


//    List<PropertySource<?>> propertySources = null;
//                try {
//        propertySources = yamlPropertySourceLoader.load(null, new FileSystemResource(file));
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }
//    var map = propertySources.stream().collect(Collectors.toMap(PropertySource::getName, Function.identity(),WebApplicationRunner.this::biConsumer));
//
//
//    ConfigurableEnvironment environment = context.getEnvironment();
//    Set<String> activeProfiles = Arrays.stream(environment.getActiveProfiles()).collect(Collectors.toSet());
//
//
//    var envMap = environment.getPropertySources().stream().collect(Collectors.toMap(PropertySource::getName, Function.identity(),WebApplicationRunner.this::biConsumer));
//    Set<String> keys = new HashSet<>();
//                map.values().forEach(i->{
//        if(!activeProfiles.contains(i.getName())) {
//            return;
//        }
//        PropertySource<?> propertySource = envMap.get(i.getName());
//        if(propertySource instanceof MapPropertySource oldMap && i instanceof MapPropertySource newMap) {
//            Map<String, Object> source = oldMap.getSource();
//            Map<String, Object> source1 = newMap.getSource();
//            keys.addAll(compareMap(source, source1));
//        }
//    });

}
