package com.sailing.feignhandler.hystrix;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2020-08-15 23:30:
 **/
@Slf4j
@Component
public class TestServiceFallback implements FallbackFactory<FeignSampleFallback> {

    @Autowired
    private FeignSampleFallback feignSampleFallback;

    @Override
    public FeignSampleFallback create(Throwable throwable) {
        log.info("初始化Throwable对象值为:>{}", throwable.getMessage());
        if (throwable.getMessage() != null){
            log.info("==>{}",throwable.fillInStackTrace());
            // 需要解析对应的url、参数、返回值主要针对异常数据
            // 实例数据格式为
            /**
             * 捕获到的异常信息为:>status 500 reading Sabnormal#queryCatAlarmData(); content:
             * {"timestamp":"2020-08-16T04:45:28.924+0000","status":500,"error":"Internal Server Error",
             * "message":"Request processing failed; nested exception is java.lang.NumberFormatException: For input string: \"s\"",
             * "path":"/querycatalarmdata/v1"}
             */
            log.info("捕获到的异常信息为==>{}", throwable.getMessage());
        }
        return feignSampleFallback;
    }
}
