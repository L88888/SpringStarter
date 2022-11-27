package com.sailing.linkstrack;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @program: spring-starter
 * @description: 实时链路追踪，主要针对一切华为API接口的请求报文、响应报文进行追踪.
 * @author: LIULEI
 * @create: 2021-04-13 14:03:
 **/
@Aspect
@Component
@Slf4j
public class RealLinkStrack {

    @Autowired
    private SinkApiInfo sinkApiInfo;

    /**
     * 切点：FeignClient接口，为了获取响应字符串
     * execution(* com.sailing.feignhandler..*(..))
     */
    private final String pointcutFeign = "execution(* com.sailing.feignhandler.manager..*(..))";

    /**
     * Feign接口切点
     */
    @Pointcut(value = pointcutFeign)
    public void logFeign() {}

    /**
     * 记录Feign调用的方法签名
     */
    String methodName = "";

    /**
     * 开始执行函数的时间
     */
    Date exeTime = null;

    /**
     * 采用通知环绕的方式获取入参、以及接口返回值对象信息
     * @param joinPoint
     */
    @Around(value = "logFeign()")
    public Object beforeAdvice(ProceedingJoinPoint joinPoint){
        // 不在切面中做任何业务处理,只记录FeignClient的入参与返回值最终给服务最终;
        Object resData = null;
        try {
            exeTime = new Date();
            long startTime = System.currentTimeMillis();
            methodName = joinPoint.getSignature().getDeclaringTypeName() + "(" + joinPoint.getSignature().getName() + ")";

            // 调用并执行具体的业务方法
            resData = joinPoint.proceed(joinPoint.getArgs());
            long endTime = System.currentTimeMillis();
            log.info("My Aop.切入FeignClient::调用方法==>{},执行耗时==>{}(ms);", methodName, endTime - startTime);

            // 透传参数
            sinkApiInfo.addApiInfoData(resData, methodName, joinPoint.getArgs(), exeTime, endTime - startTime);
            return resData;
        } catch (Throwable throwable) {
            log.info("My Aop.切入FeignClient::调用方法==>{},执行异常==>{};", methodName, throwable);
        }finally {
            // 主线程的业务不会受阻
            return resData;
        }
    }
}
