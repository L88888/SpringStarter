package com.sailing.cglib;

//
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;



import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @program: spring-starter
 * @description: 方法拦截策略捕获方法中请求与响应报文
 * @author: LIULEI
 * @create: 2021-04-27 20:23:
 **/
public class CgLibMethodInterceptor implements MethodInterceptor {

    /**
     * 使用cglib,Enhancer对象创建目标对象实例
     * @param sourceObject 源对象
     * @return
     */
    public Object createProxyInstance(Class sourceObject){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(sourceObject);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 方法拦截
        System.out.println("请求参数报文==>" + objects == null ? "" : Arrays.toString(objects));
        // 动态执行方法
        Object resObject = methodProxy.invokeSuper(o, objects);
        System.out.println("响应报文==>" + resObject);
        if (resObject == null){
            System.out.println("改变返回值");
            return "update -008";
        }else {
            System.out.println("响应报文==>" + resObject.toString());
            return "返回一个固定值.";
        }
    }
}
