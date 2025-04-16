package com.git.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

/**
 * @author authorZhao
 * @since 2025-04-08
 */
@Slf4j
public class ConfigUtil {

    public static List<File> parseArgsFiles(String[] args) {
        String s = Arrays.stream(args).filter(i -> i.contains("--spring.config.location=")).findFirst().orElse(null);
        if (StringUtils.isBlank(s)) {
            return List.of();
        }
        var path = s.split("=")[1];

        var paths = List.of(path.split(","));
        if (CollectionUtils.isEmpty(paths)) {
            return List.of();
        }
        return paths.stream().map(ConfigUtil::pathToFile)
                .map(ConfigUtil::fileToDir).
                filter(Objects::nonNull).filter(File::exists).collect(Collectors.toList());
    }

    public static List<File> parseEnvFiles(String arg) {
        if (StringUtils.isBlank(arg)) {
            return List.of();
        }
        var paths = List.of(arg.split(","));
        if (CollectionUtils.isEmpty(paths)) {
            return List.of();
        }
        return paths.stream().map(ConfigUtil::pathToFile)
                .map(ConfigUtil::fileToDir).
                filter(Objects::nonNull).filter(File::exists).collect(Collectors.toList());
    }

    public static File pathToFile(String path) {
        try {
            return new File(URI.create(path).toURL().getFile());
        } catch (MalformedURLException e) {
            log.info(e.getMessage());
            return null;
        }

    }


    public  static  <T> T biConsumer(T oldData, T newData) {
        return newData;
    }

    public static File fileToDir(File file) {
        if (file == null) {
            return null;
        }
        if (file.isDirectory()) {
            return file;
        } else {
            return file.getParentFile();
        }
    }


    // 解析 YAML 文件为扁平化的 Key-Value 集合
    public static Map<String, Object> parseYamlToFlatMap(InputStream yamlStream) {
        Yaml yaml = new Yaml();
        Map<String, Object> yamlMap = yaml.load(yamlStream);
        return flattenMap(yamlMap);
    }

    // 将嵌套的 Map 转换为扁平化结构（如 spring.datasource.url）
    private static Map<String, Object> flattenMap(Map<String, Object> source) {
        Map<String, Object> result = new HashMap<>();
        flatten("", source, result);
        return result;
    }

    private static void flatten(String prefix, Map<String, Object> source, Map<String, Object> result) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                flatten(key, (Map<String, Object>) value, result);
            } else {
                result.put(key, value);
            }
        }
    }

    // 对比新旧配置，返回变化的 Key 集合
    public static Set<String> compareConfigs(
            ConfigurableEnvironment environment,
            Map<String, Object> newConfig
    ) {
        Set<String> changedKeys = new HashSet<>();
        Map<String, Object> oldConfig = new HashMap<>();

        // 获取当前环境的所有配置
        environment.getPropertySources().stream()
                .filter(ps -> ps instanceof MapPropertySource)
                .map(ps -> ((MapPropertySource) ps).getSource().entrySet())
                .flatMap(Set::stream)
                .forEach(entry -> oldConfig.put(entry.getKey(), entry.getValue()));

        // 比较新增或修改的 Key
        newConfig.forEach((key, newValue) -> {
            Object oldValue = oldConfig.get(key);
            if (!Objects.equals(newValue, oldValue)) {
                changedKeys.add(key);
            }
        });

        // 检查被删除的 Key
        oldConfig.keySet().stream()
                .filter(key -> !newConfig.containsKey(key))
                .forEach(changedKeys::add);

        return changedKeys;
    }

}
