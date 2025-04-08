package com.git.blog.util;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.InputStream;
import java.util.*;

import org.yaml.snakeyaml.Yaml;

/**
 * @author authorZhao
 * @since 2025-04-08
 */
public class ConfigUtil {


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
