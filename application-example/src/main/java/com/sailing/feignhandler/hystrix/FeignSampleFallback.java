package com.sailing.feignhandler.hystrix;

import com.sailing.feignhandler.client.Sabnormal;
import com.sailing.linkstrack.ServiceTrackData;
import com.sailing.linkstrack.bo.ThirdPartyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2020-08-15 23:27:
 **/
@Slf4j
@Component
public class FeignSampleFallback implements Sabnormal,ServiceTrackData {

    @Override
    public Map queryCatAlarmData() {
        log.info("下游接口异常,queryCatAlarmData()");
        return null;
    }

    @Override
    public Map gatherV1(ThirdPartyLog thirdPartyLog) {
        log.info("下游服务追踪接口异常,gatherV1(),服务追踪业务数据为:>{}", thirdPartyLog);
        return null;
    }
}
