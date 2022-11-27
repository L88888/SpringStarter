package com.sailing.feignhandler.client;

import com.sailing.feignhandler.hystrix.FeignSampleFallback;
import com.sailing.feignhandler.hystrix.TestServiceFallback;
import com.sailing.linkstrack.bo.ThirdPartyLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @program: spring-starter
 * @description: 测试验证500状态下的异常信息捕获
 * @author: LIULEI
 * @create: 2020-08-15 23:12:
 **/
@Component
@FeignClient(name = "server1",
        url = "127.0.0.1:20210",
        fallback = FeignSampleFallback.class,
        fallbackFactory = TestServiceFallback.class
)
public interface Sabnormal {

    @RequestMapping(value = "querycatalarmdata/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map queryCatAlarmData();

    @PostMapping(path = "/gather/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map gatherV1(@RequestBody ThirdPartyLog thirdPartyLog);
}
