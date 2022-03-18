package com.git.blog.spring.asm;

/**
 * 自定义类加载器
 */
public class BeanMapperClassLoader extends ClassLoader {
    private static BeanMapperClassLoader myClassLoader = null;
    private BeanMapperClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
        check();
    }

    synchronized private void check() {
        if(myClassLoader!=null){
            throw new RuntimeException();
        }
    }

    synchronized public static BeanMapperClassLoader instance(){
        if(myClassLoader==null){
            myClassLoader = new BeanMapperClassLoader();
        }
        return myClassLoader;
    }


    /**
     * 吧class数组转化为Class对象
     * @param name 类全名
     * @param data class数组
     * @return
     */
    public Class<?> defineClassForName(String name, byte[] data) {
        return this.defineClass(name, data, 0, data.length);
    }

}
