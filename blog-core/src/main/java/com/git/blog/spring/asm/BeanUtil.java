package com.git.blog.spring.asm;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * bean的复制类
 */
@Slf4j
public class BeanUtil {

    private static Map<String,BeanCopyInteface> map = new HashMap<>(128);

    public static <T> T copy(Object source,Class<T> clazz){
        BeanCopyInteface beanCopyInteface = loadMap(source.getClass(),clazz);
        //log.info("当前代理类名:{},hashCode={}",beanCopyInteface.getClass().getName(),beanCopyInteface.hashCode());
        return beanCopyInteface.copyClass(source, clazz);
    }

    private static <T> BeanCopyInteface loadMap(Class sourceType,Class<T> returnType) {

        String key = sourceType.getName()+returnType.getName();
        String name = key.replace(".","");
        try {
            if(map.containsKey(key)){
                return map.get(key);
            }
            byte[] classData = BeanCopyDump.dump(sourceType,returnType);
            Class<?> clazz2 = BeanMapperClassLoader.instance().defineClassForName(AsmUtil.generateorClassName(BeanCopyInteface.class,name).replace("/","."), classData);
            BeanCopyInteface beanCopyInteface = (BeanCopyInteface)clazz2.getDeclaredConstructors()[0].newInstance();
            map.put(key,beanCopyInteface);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map.get(key);
    }

    public static <T> T copy(Object source,T t){
        BeanCopyInteface beanCopyInteface = loadMap(source.getClass(),t.getClass());
        return beanCopyInteface.copyObject(source,t);
    }



}
