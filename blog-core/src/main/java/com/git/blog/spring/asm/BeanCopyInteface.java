package com.git.blog.spring.asm;

/**
 * 复制接口，两个类型必须有get/set方法，数据类型必须一致
 * @author authorZhao
 */
public interface BeanCopyInteface{
    /**
     * 根据类型复制
     * @param source 原对象
     * @param clazz 目标对象类型
     * @param <T> 目标对象类型
     * @return
     */
    <T> T copyClass(Object source,Class<T> clazz);

    /**
     * 将前者数据拷贝到后者
     * @param source 原对象
     * @param t 目标对象
     * @param <T> 目标对象类型
     * @return
     */
    <T> T copyObject(Object source,T t);

}
