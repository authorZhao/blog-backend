package com.git.blog.spring.asm;


import net.bytebuddy.jar.asm.AnnotationVisitor;
import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author authorZhao
 * @date 2019/12/30
 */
public class BeanMapperDump implements Opcodes {


    public static byte[] dump(Class clazz) throws Exception {

        String name = clazz.getName();

        if(!clazz.isInterface()){
            throw new RuntimeException("clazz:"+ name +" is not interface");
        }
        ClassWriter cw = new ClassWriter(org.objectweb.asm.ClassWriter.COMPUTE_MAXS);
        String fileName = name.substring(name.lastIndexOf(".")+1)+"ImplProxy";
        String sourceName = fileName+".java";
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, AsmUtil.generateorClassName(clazz), null, "java/lang/Object", new String[]{AsmUtil.getClassSlashName(clazz)});

        cw.visitSource(sourceName, null);
        //不关闭继承的方法
        Method[] declaredMethods = clazz.getDeclaredMethods();

        //1.构造方法
        generateorConstructor(cw);
        Arrays.stream(declaredMethods).filter(i->i.getReturnType()!=void.class && i.getParameterCount()==1).forEach(m->{
            generateorMethod(m,cw,"L"+AsmUtil.generateorClassName(clazz));
        });
        return cw.toByteArray();
    }

    /**
     * 生成构造方法
     * @param cw
     */
    private static void generateorConstructor(ClassWriter cw) {
        MethodVisitor methodVisitor = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();

    }

    private static void generateorMethod(Method m, ClassWriter cw,String name) {
        AnnotationVisitor av0;
        MethodVisitor methodVisitor;
        //返回类型
        Class<?> returnType = m.getReturnType();

        Map<String, Method> setMethod = Arrays.stream(returnType.getDeclaredMethods()).filter(i -> i.getName().contains("set") && i.getParameterCount() == 1).collect(Collectors.toMap(i -> i.getName().substring(1), Function.identity(), (o, n) -> n));

        //参数类型
        Class<?> parameterType = m.getParameters()[0].getType();

        Map<String, Method> getMethod = Arrays.stream(parameterType.getDeclaredMethods()).filter(i -> i.getName().contains("get") && i.getParameterCount() == 0).collect(Collectors.toMap(i -> i.getName().substring(1), Function.identity(), (o, n) -> n));
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



        //1.创建对象
        {
            methodVisitor = cw.visitMethod(ACC_PUBLIC, m.getName(), AsmUtil.getMethodDescriptor(m), null, null);
            methodVisitor.visitParameter(m.getParameters()[0].getName(), 0);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, AsmUtil.getClassSlashName(returnType));
            methodVisitor.visitInsn(DUP);

            methodVisitor.visitMethodInsn(INVOKESPECIAL, AsmUtil.getClassSlashName(returnType), "<init>", "()V", false);
            methodVisitor.visitVarInsn(ASTORE, 2);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitVarInsn(ALOAD, 1);

            //2.循环方法
            int j = 1;
            for (MethodGetSet i : list) {
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, AsmUtil.getClassSlashName(parameterType), i.getGetMethod(), AsmUtil.getMethodDescriptor(i.getMethodGet()), false);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, AsmUtil.getClassSlashName(returnType), i.getSetMethod(), AsmUtil.getMethodDescriptor(i.getMethodSet()), false);
                methodVisitor.visitVarInsn(ALOAD, 2);
                if (j == list.size()) {
                    methodVisitor.visitInsn(ARETURN);
                    //这个可能存在栈溢出
                    methodVisitor.visitMaxs(20, 3);
                    methodVisitor.visitEnd();
                } else {
                    methodVisitor.visitVarInsn(ALOAD, 1);
                }
                j++;
            }
        }

        //3.返回
        cw.visitEnd();
    }


}
