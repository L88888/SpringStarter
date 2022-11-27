package com.sailing.cglib.test;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @program: spring-starter
 * @description: 方法拦截策略捕获方法中请求与响应报文
 * @author: LIULEI
 * @create: 2021-04-27 20:23:
 **/
public class CgLibMethodInterceptor implements MethodInterceptor {

    // 代理的目标对象
    private Object targetObject;

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
        System.out.println(objects.toString());

        // 动态执行方法
        Object resObject = methodProxy.invokeSuper(o, objects);
        System.out.println("响应报文==>" + resObject);
        return resObject;
    }
}
