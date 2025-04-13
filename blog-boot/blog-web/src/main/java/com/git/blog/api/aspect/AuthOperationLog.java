package com.git.blog.api.aspect;

import com.alibaba.fastjson2.JSON;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.Permission;
import com.git.blog.dto.auth.PermissionData;
import com.git.blog.dto.model.entity.Menu;
import com.git.blog.exception.ApiUnauthorizedException;
import com.git.blog.service.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户体系操作记录
 *
 * @author authorZhao
 * @since 2021-01-12
 */
@Aspect
@Component
@Slf4j
public class AuthOperationLog {

    private static final Cache<String, PermissionData> LOCAL_CACHE = Caffeine.newBuilder()
            .maximumSize(50000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Autowired
    private UserService userService;

    @Pointcut("within(com.git.blog.api.controller.*.UserController || com.git.blog.api.controller.*.MenuController || com.git.blog.api.controller.*.RoleController)")
    public void pointCut() {

    }

    @Pointcut("execution(* com.git.blog.api.controller..*Controller.*(..))")
    public void permissionCut() {

    }

    @Before("permissionCut()")
    public void permissionCheck(JoinPoint joinPoint) {

        var signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature methodSignature)) {
            throw new ApiUnauthorizedException("类型不对没有权限");
        }

        Method method = methodSignature.getMethod();
        var permissionData = LOCAL_CACHE.get(signature.toShortString(), i -> buildFromMethod(method));
        if (permissionData == null) {
            throw new ApiUnauthorizedException("类型找不到没有权限");
        }

        Permission permission = permissionData.permission();
        if (permission == null || permission.open()) {
            return;
        }
        Set<String> urls = permissionData.urls();

        List<Menu> menuTreeVOList = userService.getUserAllMenu(AuthTheadLocal.get());
        //classMapping +methodMapping == permission.url()
        boolean hasPermission = false;
        String permissionUrl = permission.url();

        if (StringUtils.isNotBlank(permissionUrl)) {
            hasPermission = menuTreeVOList.stream().anyMatch(i -> permissionUrl.equals(i.getMenuUrl()));
        } else {
            hasPermission = menuTreeVOList.stream().anyMatch(i -> urls.stream().anyMatch(u -> i.getMenuUrl().equals(u)));
        }

        if (!hasPermission) {
            //throw new ApiUnauthorizedException(StringUtils.isNoneBlank(permission.message()) ? permission.message() : "没有权限");
        }
    }


    /**
     * 日志记录
     *
     * @param joinPoint
     * @return
     */
    @Around("pointCut()")
    public Object logRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.info("权鉴模块操作，uid={}参数{}", AuthTheadLocal.get(), args);
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
        log.info("权鉴模块操作结果{}", JSON.toJSONString(result));
        return result;
    }


    private PermissionData buildFromMethod(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();

        Permission permission = method.getAnnotation(Permission.class);
        if (permission == null) {
            permission = declaringClass.getAnnotation(Permission.class);
        }

        var classUrls = acquireUrls(declaringClass);
        var methodUrls = acquireUrls(method);

        Set<String> urls = new HashSet<>();
        if (CollectionUtils.isNotEmpty(classUrls)) {
            for (String classUrl : classUrls) {
                for (String s : methodUrls) {
                    urls.add(classUrl + s);
                }
            }
        } else {
            urls.addAll(methodUrls);
        }
        return new PermissionData(toShortName(method), declaringClass, method, permission, urls);
    }

    private Set<String> acquireUrls(AnnotatedElement annotatedElement) {
        Set<RequestMapping> classRequestMappings = AnnotatedElementUtils.findAllMergedAnnotations(annotatedElement, RequestMapping.class);
        return classRequestMappings.stream().flatMap(i -> Arrays.stream(i.value()))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
    }

    private String toShortName(Method method) {
        StringJoiner sj = new StringJoiner(",", "(", ")");
        for (Class<?> parameterType : method.getParameterTypes()) {
            sj.add(parameterType.getTypeName());
        }
        return method.getClass().getTypeName() + "#" + method.getName() + sj.toString();
    }
}
