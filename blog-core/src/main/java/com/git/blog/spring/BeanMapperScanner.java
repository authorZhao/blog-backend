package com.git.blog.spring;

import com.git.blog.spring.factorys.BeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * @author authorZhao
 * @date 2019/12/13
 */
public class BeanMapperScanner extends ClassPathBeanDefinitionScanner {

    public BeanMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    /**
     * 扩大一点权限
     * @param basePackages
     * @return
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        for (BeanDefinitionHolder holder : beanDefinitions) {
            try {
                GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
                ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
                constructorArgumentValues.addIndexedArgumentValue(0,Class.forName(definition.getBeanClassName()));
                definition.getConstructorArgumentValues().addArgumentValues(constructorArgumentValues);
                definition.setBeanClass(BeanMapperFactoryBean.class);
                definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return beanDefinitions;
    }

    @Override
    protected void registerDefaultFilters() {
        this.addIncludeFilter((metadataReader,metadataReaderFactory)->{
            if(metadataReader.getClassMetadata().isInterface())return true;
            return false;
        });
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
