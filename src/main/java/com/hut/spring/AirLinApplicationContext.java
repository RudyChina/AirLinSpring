package com.hut.spring;

import cn.hutool.core.util.StrUtil;
import com.hut.spring.annotation.Component;
import com.hut.spring.annotation.ComponentScan;
import com.hut.spring.annotation.Scope;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 上下文容器
 * @Author: zlf
 * @date: 2022-04-26
 */
public class AirLinApplicationContext {

    private Class appConfigClass;


    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    public AirLinApplicationContext(Class appConfigClass) {
        this.appConfigClass = appConfigClass;
        //扫描类文件

        List<Class> beanClasses = new ArrayList<>();
        //扫描Bean
        if (appConfigClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan scanAnnotation = (ComponentScan) appConfigClass.getAnnotation(ComponentScan.class);
            //获取ComponentScan注解扫描包名
            String scanPackagePath = scanAnnotation.value();
            if (StrUtil.isBlank(scanPackagePath)) {
                throw new RuntimeException("Annotation on class AppConfig has no value,Please check class named AppConfig");
            }
            //当前操作系统的文件分隔符
            String fileSeparator = System.getProperty("file.separator");
            scanPackagePath = scanPackagePath.replaceAll("\\.",fileSeparator);
            ClassLoader appClassLoader = appConfigClass.getClassLoader();
            URL resource = appClassLoader.getResource(scanPackagePath);
            File file = new File(resource.getFile());
            loopResolveClass(file,fileSeparator,appClassLoader,beanClasses);
            System.out.println(beanClasses);
        }else {
            throw new RuntimeException("No annotation called ComponentScan Configured,Please check class named AppConfig");
        }
        //封装beanDefinitionMap
        beanClasses.forEach(bean->{
            //key->beanName(取注解值，若无取首字母小写值)
            String beanName = ((Component)bean.getAnnotation(Component.class)).value();
            if (StrUtil.isBlank(beanName)) {
                beanName = StrUtil.lowerFirst(bean.getName());
            }
            BeanDefinition beanDefinition = new BeanDefinition();
            if (bean.isAnnotationPresent(Scope.class)) {
                Scope scopeAnnotation = (Scope) bean.getAnnotation(Scope.class);
                String scopeVal = scopeAnnotation.value();
            }
            //beanDefinition.setScope();
            beanDefinition.setBeanType(bean);
        });
    }

    /**
     * 递归解析classes下类文件
     * @param file
     * @return
     */
    private void loopResolveClass(File file,String fileSeparator,ClassLoader appClassLoader,List<Class> beanClasses) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    loopResolveClass(f,fileSeparator,appClassLoader,beanClasses);
                }else{
                    if (f.getName().endsWith(".class")) {
                        String fileAbsolutePath = f.getAbsolutePath();
                        String classPath = fileAbsolutePath.substring(fileAbsolutePath.indexOf("com"), fileAbsolutePath.indexOf(".class"));
                        classPath = classPath.replaceAll(fileSeparator, "\\.");
                        Class<?> clazz = null;
                        try {
                            clazz = appClassLoader.loadClass(classPath);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        beanClasses.add(clazz);
                    }else{
                        System.out.println("not class file,resolve skip");
                    }
                }

            }
        }
    }


    public Object getBean(String beanName){
        return null;
    }
}
