package com.sailing.linkstrack;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.annotation.Annotation;
/**
 * @program: spring-starter
 * @description: 计算Feign接口请求耗时，通过AOP切面的方式进行捕获接口请求、响应时长
 * @author: LIULEI
 * @create: 2021-04-13 15:54:
 **/
//@Component
//@EnableAspectJAutoProxy
@Slf4j
public class FeignCostTime extends AbstractPointcutAdvisor{

    public FeignCostTime(){
        log.info("计算FeignClient接口请求耗时.");
    }

    /**
     * 过滤符合条件的Feign对象信息
     * @param cs
     * @param annotation
     * @return
     */
    public static boolean existsAnnotation(Class<?> cs, Class<? extends Annotation> annotation) {
        log.info("过滤符合条件的Feign对象信息:>{}", cs.toString());
        boolean rs = cs.isAnnotationPresent(annotation);
        if (!rs) {
            rs = cs.getSuperclass() != null && existsAnnotation(cs.getSuperclass(), annotation);
        }
        if (!rs) {
            Class<?>[] interfaces = cs.getInterfaces();
            for (Class<?> f : interfaces) {
                rs = existsAnnotation(f, annotation);
                if (rs) {
                    break;
                }
            }
        }
        if (!rs) {
            Annotation[] annotations = cs.getAnnotations();
            for (Annotation an : annotations) {
                rs = annotation.isAssignableFrom(an.getClass());
                if (rs) {
                    break;
                }
            }
        }
        return rs;
    }

    /**
     * 直接切入FeignClient的端点对象
     * @return
     */
    @Override
    public Pointcut getPointcut() {
        return new Pointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return (Class<?> clazz) -> existsAnnotation(clazz, FeignClient.class);
            }

            @Override
            public MethodMatcher getMethodMatcher() {
                return MethodMatcher.TRUE;
            }
        };
    }

    /**
     * 通过方法拦截的方式来执行对应的业务方法Method
     * @return
     */
    @Override
    public Advice getAdvice() {
        return (MethodInterceptor)(MethodInvocation invocation) -> {
            // 获取方法方法的基本信息
            log.info("FeignClient方法调用执行的信息为:>{}", invocation.toString());
//            invocation.getMethod();
//            invocation.toString();
            long startTime = System.currentTimeMillis();
            Object resData = invocation.proceed();
            long endTime = System.currentTimeMillis();
            log.info("FeignClient方法{},调用执行的时间为:>{}{}毫秒.", invocation.getMethod(), endTime - startTime);
            return resData;
        };
    }
}
