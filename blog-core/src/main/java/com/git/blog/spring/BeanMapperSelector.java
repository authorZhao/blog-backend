package com.git.blog.spring;

import com.git.blog.spring.anno.BeanMapperScan;
import com.git.blog.spring.asm.BeanMapperClassLoader;
import com.git.blog.spring.factorys.BeanMapperFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;


/**
 * 注册bean的
 */
@Slf4j
public class BeanMapperSelector implements ImportBeanDefinitionRegistrar {

    /**
     * 扫描的包路径
     */
    private String[] basePackage;
    /**
     * 需要扫描的类
     */
    private Class[] classes;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Thread.currentThread().setContextClassLoader(BeanMapperClassLoader.instance());
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(BeanMapperScan.class.getName());
        this.basePackage = (String[])annotationAttributes.get("basePackages");

        this.classes = (Class[])annotationAttributes.get("classes");
        BeanMapperScanner mapperScanner = new BeanMapperScanner(registry);
        log.info("开始扫描包：{}",basePackage);
        if(basePackage!=null&&basePackage.length>0) {
            mapperScanner.doScan(this.basePackage);
        }
        log.info("开始扫描类：{}",classes);

        if(classes==null || classes.length<=0){
            return;
        }

        for (Class clzz:classes){
            if(!clzz.isInterface())continue;
            if(!registry.containsBeanDefinition(clzz.getSimpleName())){
                try {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clzz);
                    GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                    //这里采用的是byType方式注入，类似的还有byName等
                    definition.setBeanClass(BeanMapperFactoryBean.class);
                    ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                    constructorArgumentValues.addIndexedArgumentValue(0,clzz);
                    definition.getConstructorArgumentValues().addArgumentValues(constructorArgumentValues);
                    definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                    registry.registerBeanDefinition(clzz.getSimpleName(), definition);
                    log.info("{}定义信息注册完毕", clzz.getSimpleName());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
