package com.git.blog.api.aspect;

import com.alibaba.fastjson.JSON;
import com.git.blog.commmon.enums.AuthTheadLocal;
import com.git.blog.config.Permission;
import com.git.blog.dto.model.entity.Menu;
import com.git.blog.exception.ApiUnauthorizedException;
import com.git.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户体系操作记录
 * @author authorZhao
 * @since 2021-01-12
 */
@Aspect
//@Component
@Slf4j
public class AuthOperationLog {

    @Autowired
    private UserService userService;

    @Pointcut("within(com.git.blog.api.controller.UserController || com.git.blog.api.controller.MenuController || com.git.blog.api.controller.RoleController)")
    public void pointCut(){

    }

    @Pointcut("execution(* com.git.blog.api.controller.*.*(..))")
    public void permissionCut(){

    }

    @Before("permissionCut()")
    public void permissionCheck(JoinPoint joinPoint){
        Class<?> declaringType = joinPoint.getSignature().getDeclaringType();
        List<Method> collect = Arrays.stream(declaringType.getDeclaredMethods()).filter(i -> i.getName().equals(joinPoint.getSignature().getName())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect) || collect.size()>1){
            log.warn("存在同名方法，或者方法不存在{}",collect);
        }
        Method method = collect.get(0);
        if(!method.isAnnotationPresent(Permission.class) && !declaringType.isAnnotationPresent(Permission.class)){
            return;
        }
        Permission permission = method.getAnnotation(Permission.class);
        if(permission==null){
            permission = declaringType.getAnnotation(Permission.class);
        }

        if(!permission.open()){
            return;
        }
        RequestMapping classMapping = declaringType.getAnnotation(RequestMapping.class);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        PostMapping postMapping = method.getAnnotation(PostMapping.class);

        String[] methodUrls  = null;

        if(requestMapping!=null){
            methodUrls = requestMapping.value();
        }else if(getMapping!=null){
            methodUrls = getMapping.value();
        }else if(postMapping !=null){
            methodUrls = postMapping.value();
        }else {
            //考虑开放类注解，存在没有注解的方法,直接跳过
            return;
        }

        String[] classUrls = classMapping.value();
        Set<String> urls = new HashSet<>();
        for (String classUrl : classUrls) {
            for (String s : methodUrls) {
                urls.add(classUrl+s);
            }
        }

        List<Menu> menuTreeVOList = userService.getUserAllMenu(AuthTheadLocal.get());
        //classMapping +methodMapping == permission.url()
        boolean hasPermission = false;
        String permissionUrl = permission.url();

        if(StringUtils.isNotBlank(permissionUrl)){
            hasPermission = menuTreeVOList.stream().anyMatch(i ->permissionUrl.equals(i.getMenuUrl()));
        }else{
            hasPermission = menuTreeVOList.stream().anyMatch(i ->urls.stream().anyMatch(u->i.getMenuUrl().equals(u)));
        }

        if(!hasPermission){
            throw new ApiUnauthorizedException(StringUtils.isNoneBlank(permission.message())?permission.message():"没有权限");
        }
    }


    /**
     * 日志记录
     * @param joinPoint
     * @return
     */
    @Around("pointCut()")
    public Object logRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.info("权鉴模块操作，uid={}参数{}",AuthTheadLocal.get(),args);
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
}
