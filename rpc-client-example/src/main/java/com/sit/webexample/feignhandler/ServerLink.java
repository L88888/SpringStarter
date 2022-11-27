package com.sit.webexample.feignhandler;

import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @program: spring-starter
 * @description: 测试验证500状态下的异常信息捕获
 * @author: LIULEI
 * @create: 2020-08-15 23:12:
 **/
@Component
@FeignClient(name = "server1",
        url = "127.0.0.1:12002"
//        fallback = FeignSampleFallback.class,
//        fallbackFactory = TestServiceFallback.class
)
public interface ServerLink {

    /**
     * curl http://127.0.0.1:12002/v1/testrequest
     * @return
     */
    @RequestMapping(value = "v1/testrequest",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response testHttp(@RequestBody Request dataParam);
}
