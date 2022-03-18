package com.git.blog.spring.factorys;

import com.git.blog.spring.asm.AsmUtil;
import com.git.blog.spring.asm.BeanMapperClassLoader;
import com.git.blog.spring.asm.BeanMapperDump;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * 为Mapper生成代理对象
 * @author authorZhao
 * @param <T>
 */
@Slf4j
public class BeanMapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> interfaceClass;

    public BeanMapperFactoryBean(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public BeanMapperFactoryBean() {
    }


    /**
     * 这里指定代理的逻辑
     * @return
     * @throws Exception
     */
    @Override
    public T getObject() throws Exception {
        log.info("正在为{}生成代理对象",interfaceClass.getName());
        byte[] classData = BeanMapperDump.dump(interfaceClass);
        Class<?> impl = BeanMapperClassLoader.instance().defineClassForName(AsmUtil.generateorClassName(interfaceClass).replace("/", "."), classData);
        //输出class文件
        //ProxyGenerator.writeClassToFile(new File("").getAbsolutePath()+"/"+AsmUtil.generateorClassName(interfaceClass)+".class",classData);
        T t = (T) impl.getConstructors()[0].newInstance();
        if(t==null){
            log.error("{}代理对象,生成失败",interfaceClass.getName());
        }
        log.info("{}代理对象,生成成功",interfaceClass.getName());
        return t;
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 如果采用方法注入属性，必须准备无参构造，不然报错
     * @param serviceInterface
     */
    public void setServiceInterface(Class<T> serviceInterface) {
        this.interfaceClass = serviceInterface;
    }
}
