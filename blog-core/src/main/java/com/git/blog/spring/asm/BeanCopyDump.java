package com.git.blog.spring.asm;



import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * bean复制的工具类
 */
public class BeanCopyDump implements Opcodes {

    public static byte[] dump(final Class sourceType,final Class returnType) throws Exception {
        Class clazz = BeanCopyInteface.class;

        String name = clazz.getName();

        if(!clazz.isInterface()){
            throw new RuntimeException("clazz:"+ name +" is not interface");
        }

        //1.类属性设置，采用计算形式，损失一些性能
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        String fileName = name.substring(name.lastIndexOf(".")+1)+"ImplProxy";
        String sourceName = fileName+".java";

        String key = sourceType.getName()+returnType.getName();
        String str = key.replace(".","");

        cw.visit(55, ACC_PUBLIC + ACC_SUPER, AsmUtil.generateorClassName(clazz,str), null, "java/lang/Object", new String[]{AsmUtil.getClassSlashName(clazz)});
        cw.visitSource(sourceName, null);

        Method[] declaredMethods = clazz.getDeclaredMethods();

        //2.构造方法
        generateConstructors(cw);

        //3.成员方法
        Arrays.stream(declaredMethods).forEach(m->{
            if("copyClass".equals(m.getName())){
                generateorCopyClass(m,cw,sourceType,returnType);
            }
            if("copyObject".equals(m.getName())){
                generateorCopyObject(m,cw,sourceType,returnType);
            }
        });
        cw.visitEnd();

        return cw.toByteArray();
    }


    private static void generateConstructors(ClassWriter cw){
        MethodVisitor methodVisitor;
        methodVisitor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();
    }

    private static void generateorCopyClass(Method m, ClassWriter cw,Class sourceType,Class returnType) {

        MethodVisitor methodVisitor;
        //返回类型


        Map<String, Method> setMethod = Arrays.stream(returnType.getDeclaredMethods()).filter(i -> i.getName().contains("set") && i.getParameterCount() == 1).collect(Collectors.toMap(i -> i.getName().substring(1), Function.identity(), (o, n) -> n));

        //参数类型
        Map<String, Method> getMethod = Arrays.stream(sourceType.getDeclaredMethods()).filter(i -> i.getName().contains("get") && i.getParameterCount() == 0).collect(Collectors.toMap(i -> i.getName().substring(1), Function.identity(), (o, n) -> n));

        List<MethodGetSet> list = new ArrayList<>();
        getMethod.entrySet().forEach(i->{
            if(setMethod.containsKey(i.getKey())){
                MethodGetSet methodGetSet = new MethodGetSet();
                methodGetSet.setField(i.getKey().substring(2).toLowerCase());
                methodGetSet.setSetMethod("s"+i.getKey());
                methodGetSet.setGetMethod("g"+i.getKey());
                methodGetSet.setMethodGet(i.getValue());
                methodGetSet.setMethodSet(setMethod.get(i.getKey()));
                Class<?> returnType1 = methodGetSet.getMethodGet().getReturnType();
                Class<?> parameterType = methodGetSet.getMethodSet().getParameterTypes()[0];
                //类型检查，避免报错
                if(returnType1==parameterType){
                    list.add(methodGetSet);
                }
            }
        });
//        returnType = Object.class;
//        sourceType = Object.class;

        //1.创建对象
        {
            methodVisitor = cw.visitMethod(ACC_PUBLIC, m.getName(), AsmUtil.getMethodDescriptor(m), "<T:Ljava/lang/Object;>(Ljava/lang/Object;Ljava/lang/Class<TT;>;)TT;", null);
            methodVisitor.visitParameter(m.getParameters()[0].getName(), 0);
            methodVisitor.visitParameter(m.getParameters()[1].getName(), 0);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, AsmUtil.getClassSlashName(returnType));
            methodVisitor.visitInsn(DUP);

            methodVisitor.visitMethodInsn(INVOKESPECIAL, AsmUtil.getClassSlashName(returnType), "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitTypeInsn(CHECKCAST, AsmUtil.getClassSlashName(sourceType));

            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitVarInsn(ALOAD, 4);

            //2.循环方法
            int j = 1;
            for (MethodGetSet i : list) {
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, AsmUtil.getClassSlashName(sourceType), i.getGetMethod(), AsmUtil.getMethodDescriptor(i.getMethodGet()), false);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, AsmUtil.getClassSlashName(returnType), i.getSetMethod(), AsmUtil.getMethodDescriptor(i.getMethodSet()), false);
                if (j == list.size()) {
                    methodVisitor.visitVarInsn(ALOAD, 3);
                    methodVisitor.visitInsn(ARETURN);
                    methodVisitor.visitMaxs(2, 5);
                    methodVisitor.visitEnd();
                } else {
                    methodVisitor.visitVarInsn(ALOAD, 3);
                    methodVisitor.visitVarInsn(ALOAD, 4);
                }
                j++;
            }
        }

    }

    private static void generateorCopyObject(Method m, ClassWriter cw,Class sourceType,Class returnType) {

        MethodVisitor methodVisitor;
        //返回类型

        Map<String, Method> setMethod = Arrays.stream(returnType.getDeclaredMethods()).filter(i -> i.getName().contains("set") && i.getParameterCount() == 1).collect(Collectors.toMap(i -> i.getName().substring(1), Function.identity(), (o, n) -> n));

        //参数类型
        Map<String, Method> getMethod = Arrays.stream(sourceType.getDeclaredMethods()).filter(i -> i.getName().contains("get") && i.getParameterCount() == 0).collect(Collectors.toMap(i -> i.getName().substring(1), Function.identity(), (o, n) -> n));

        List<MethodGetSet> list = new ArrayList<>();
        getMethod.entrySet().forEach(i->{
            if(setMethod.containsKey(i.getKey())){
                MethodGetSet methodGetSet = new MethodGetSet();
                methodGetSet.setField(i.getKey().substring(2).toLowerCase());
                methodGetSet.setSetMethod("s"+i.getKey());
                methodGetSet.setGetMethod("g"+i.getKey());
                methodGetSet.setMethodGet(i.getValue());
                methodGetSet.setMethodSet(setMethod.get(i.getKey()));
                list.add(methodGetSet);
            }
        });
        //returnType = Object.class;
        //sourceType = Object.class;

        //1.创建对象
        {
            methodVisitor = cw.visitMethod(ACC_PUBLIC, m.getName(), AsmUtil.getMethodDescriptor(m), "<T:Ljava/lang/Object;>(Ljava/lang/Object;TT;)TT;", null);
            methodVisitor.visitParameter(m.getParameters()[0].getName(), 0);
            methodVisitor.visitParameter(m.getParameters()[1].getName(), 0);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 1);

            methodVisitor.visitTypeInsn(CHECKCAST, AsmUtil.getClassSlashName(sourceType));
            methodVisitor.visitVarInsn(ASTORE, 3);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitTypeInsn(CHECKCAST,  AsmUtil.getClassSlashName(returnType));

            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitVarInsn(ALOAD, 3);

            //2.循环方法
            int j = 1;
            for (MethodGetSet i : list) {
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, AsmUtil.getClassSlashName(sourceType), i.getGetMethod(), AsmUtil.getMethodDescriptor(i.getMethodGet()), false);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, AsmUtil.getClassSlashName(returnType), i.getSetMethod(), AsmUtil.getMethodDescriptor(i.getMethodSet()), false);
                if (j == list.size()) {
                    methodVisitor.visitVarInsn(ALOAD, 2);
                    methodVisitor.visitInsn(ARETURN);
                    methodVisitor.visitMaxs(2, 5);
                    methodVisitor.visitEnd();
                } else {
                    methodVisitor.visitVarInsn(ALOAD, 4);
                    methodVisitor.visitVarInsn(ALOAD, 3);
                }
                j++;
            }
        }


    }


}
