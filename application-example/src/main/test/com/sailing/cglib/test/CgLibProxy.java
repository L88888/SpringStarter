package com.sailing.cglib.test;


import org.springframework.cglib.core.DebuggingClassWriter;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2021-04-27 20:30:
 **/
public class CgLibProxy {

    public static void main(String[] ager){
        // 指定目录下生成动态代理类
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "F:\\cglib_proxy\\java_workapace");

        Face face = (Face) new CgLibMethodInterceptor().createProxyInstance(Face.class);
        // 调用最终方法，看看会不会被动态代理
        face.facePassTime("张三");
        // 调用普通方法
        face.facePassTime("李四","王五");
        // 调用普通方法
        face.faceQuery("李四、王麻子");
    }
}
