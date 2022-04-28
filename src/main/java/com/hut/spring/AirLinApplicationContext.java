package com.hut.spring;

import cn.hutool.core.util.StrUtil;
import com.hut.spring.annotation.Component;
import com.hut.spring.annotation.ComponentScan;
import com.hut.spring.annotation.Scope;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 上下文容器
 * @Author: 众码纪
 * @date: 2022-04-26
 */
public class AirLinApplicationContext {

    /** 配置类 **/
    private Class appConfigClass;

    //BeanDefinition缓存
    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //单例池
    private ConcurrentHashMap<String, Object> singleTonBeanPool = new ConcurrentHashMap<>();

    public AirLinApplicationContext(Class appConfigClass) {
        this.appConfigClass = appConfigClass;
        //1.扫描Bean，缓存
        scanBean(appConfigClass);
        //2.初始化Bean,缓存至单例池
        beanDefinitionMap.forEach((beanName,beanDefinition)->{
            Class beanClass = beanDefinition.getBeanType();
            Object bean = instanceBean(beanClass);
            if (bean == null) {
                throw new RuntimeException("reflect create Bean error");
            }
            singleTonBeanPool.put(beanName, bean);
        });
    }

    /**
     * 获取Bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        //获取bean的定义
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            //bean的作用域
            ScopeEnum scope = beanDefinition.getScope();
            if (ScopeEnum.prototype.equals(scope)) {
                //原型Bean，重新创建bean返回
                Object prototypeBean = instanceBean(beanDefinition.getBeanType());
                if (prototypeBean == null) {
                    throw new RuntimeException("instance prototype bean error");
                }
                return prototypeBean;
            }else{
                //单例Bean，从singleTonBeanPool单例池中获取
                Object singleTonBean = singleTonBeanPool.get(beanName);
                return singleTonBean;
            }
        }else{
            //非法输入的Bean
            throw new RuntimeException("bean is not exists");
        }
    }

    /**
     * 扫描Bean文件
     * @param appConfigClass
     */
    private void scanBean(Class appConfigClass) {
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
            scanPackagePath = scanPackagePath.replace(".",fileSeparator);
            ClassLoader appClassLoader = appConfigClass.getClassLoader();
            URL resource = appClassLoader.getResource(scanPackagePath);
            File file = null;
            try {
                file = new File(URLDecoder.decode(resource.getFile(),"utf-8"));//utf-8对URL编码
            } catch (UnsupportedEncodingException e) {
                System.out.println("encoding URL error,please check your encoding type");
                e.printStackTrace();
            }
            loopResolveClass(file,fileSeparator,appClassLoader,beanClasses);
            System.out.println(beanClasses);
        }else {
            throw new RuntimeException("No annotation called ComponentScan Configured,Please check class named AppConfig");
        }
        //封装beanDefinitionMap
        beanClasses.forEach(bean->{
            BeanDefinition beanDefinition = new BeanDefinition();
            //key->beanName(取注解值，若无取首字母小写值)
            String beanName = ((Component)bean.getAnnotation(Component.class)).value();
            if (StrUtil.isBlank(beanName)) {
                beanName = StrUtil.lowerFirst(bean.getSimpleName());
            }
            //作用域封装
            if (bean.isAnnotationPresent(Scope.class)) {
                Scope scopeAnnotation = (Scope) bean.getAnnotation(Scope.class);
                ScopeEnum scopeVal = scopeAnnotation.value();
                if (ScopeEnum.singleTon.equals(scopeVal)) {
                    beanDefinition.setScope(ScopeEnum.singleTon);
                }else{
                    beanDefinition.setScope(ScopeEnum.prototype);
                }
            }else{
                beanDefinition.setScope(ScopeEnum.singleTon);
            }
            //bean类型
            beanDefinition.setBeanType(bean);
            beanDefinitionMap.put(beanName,beanDefinition);
        });
    }

    /**
     * 递归解析classes下类文件，并加载至jvm
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
                        classPath = classPath.replace(fileSeparator, ".");
                        Class<?> clazz = null;
                        try {
                            //通过类加载器获取到Class字节码对象
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

    /**
     * 反射创建Bean
     * @param beanClass
     * @return
     */
    private Object instanceBean(Class beanClass) {
        Object bean = null;
        try {
            bean = beanClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }


}
