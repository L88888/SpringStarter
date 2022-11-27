package com.sailing.cglib;

//import net.sf.cglib.core.DebuggingClassWriter;

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

        // 调用类方法
        face.facePassTime("张三");
        face.faceQuery("李四、王麻子");
        String resData = face.facePassTime("王二","李志勇");
        System.out.println("resData ===>" + resData);
    }
}
