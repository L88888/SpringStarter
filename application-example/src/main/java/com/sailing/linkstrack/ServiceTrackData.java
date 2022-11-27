package com.sailing.linkstrack;

import com.sailing.linkstrack.bo.ThirdPartyLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @program: spring-starter
 * @description: 高并发场景下测试服务追踪调用，引起的业务表锁表问题
 * @author: LIULEI
 * @create: 2020-08-24 14:18:
 **/
@FeignClient(name = "serviceTrackv2",
        url = "${sit.linkstrack.restful.url}"
)
@Component
public interface ServiceTrackData {

    /**
     * API日志追踪
     * @param thirdPartyLog 第三方API日志记录对象
     * @return
     */
    @PostMapping(path = "/strac/gather/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map gatherV1(@RequestBody ThirdPartyLog thirdPartyLog);
}