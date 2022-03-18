package com.git.blog.spring.asm;

import java.lang.reflect.Method;

/**
 * asm工具类
 */
public class AsmUtil {


    /**
     * clazz
     * @param clazz com.qdz.proxy.asm.test1.User
     * @return User
     */
    public static String getClassName(Class clazz){
        String name = clazz.getName();
        return name.substring(name.lastIndexOf(".")+1);
    }

    public static String getClassSlashName(Class clazz){
        return clazz.getName().replace(".","/");
    }


    /**
     * 例如：String.class Lcom/git/SkinDTO;
     * @param clazz
     * @return
     */
    public static String getLclassName(Class clazz){
        return "L"+clazz.getName().replace(".","/")+";";
    }

    public static String generateorClassName(Class clazz){
        String name = clazz.getName();
        String fileName = name.substring(name.lastIndexOf(".")+1)+"ImplProxy";
        String filePathName = name.replace(".","/");
        return filePathName+"/"+fileName;
    }

    /**
     * 获取生成的类名
     * @param clazz
     * @param str 必须符合java的class命名规范
     * @return
     */
    public static String generateorClassName(Class clazz,String str){
        String name = clazz.getName();
        String fileName = name.substring(name.lastIndexOf(".")+1)+"ImplProxy"+str;
        String filePathName = name.replace(".","/");
        return filePathName+"/"+fileName;
    }

    public static String getMethodDescriptor(final Method method) {
        final StringBuffer buf = new StringBuffer();
        buf.append("(");
        final Class<?>[] types = method.getParameterTypes();
        for (int i = 0; i < types.length; ++i) {
            buf.append(getDesc(types[i]));
        }
        buf.append(")");
        buf.append(getDesc(method.getReturnType()));
        return buf.toString();
    }
    public static String getDesc(final Class<?> returnType) {
        if (returnType.isPrimitive()) {
            return getPrimitiveLetter(returnType);
        }
        if (returnType.isArray()) {
            return "[" + getDesc(returnType.getComponentType());
        }
        return "L" + getType(returnType) + ";";
    }
    public static String getType(final Class<?> parameterType) {
        if (parameterType.isArray()) {
            return "[" + getDesc(parameterType.getComponentType());
        }
        if (!parameterType.isPrimitive()) {
            final String clsName = parameterType.getName();
            return clsName.replaceAll("\\.", "/");
        }
        return getPrimitiveLetter(parameterType);
    }
    public static String getPrimitiveLetter(final Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            return "I";
        }
        if (Void.TYPE.equals(type)) {
            return "V";
        }
        if (Boolean.TYPE.equals(type)) {
            return "Z";
        }
        if (Character.TYPE.equals(type)) {
            return "C";
        }
        if (byte.class==type) {
            return "B";
        }
        if (short.class==type) {
            return "S";
        }
        if (float.class==type) {
            return "F";
        }
        if (long.class==type) {
            return "J";
        }
        if (double.class==type) {
            return "D";
        }
        throw new IllegalStateException("Type: " + type.getCanonicalName() + " is not a primitive type");
    }
}

