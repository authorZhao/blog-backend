package com.git.blog.dto.auth;

import com.git.blog.config.Permission;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 权限类
 * @param signature
 * @param clazz
 * @param method
 * @param permission
 * @param urls
 */
public record PermissionData(String signature, Class<?> clazz, Method method, Permission permission, Set<String> urls) {
}
