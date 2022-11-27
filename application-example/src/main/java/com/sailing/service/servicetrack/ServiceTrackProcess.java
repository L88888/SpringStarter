package com.sailing.service.servicetrack;

import com.alibaba.fastjson.JSONObject;
import com.sailing.comm.HttpClientServer;
import com.sailing.linkstrack.ServiceTrackData;
import com.sailing.linkstrack.bo.ThirdPartyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring-starter
 * @description: 服务追踪业务处理类
 * @author: LIULEI
 * @create: 2020-08-24 19:49:
 **/
@Slf4j
@Service
public class ServiceTrackProcess {

    @Autowired
    private ServiceTrackData serviceTrackData;

    /**
     * 调用服务追踪，开始记录服务之间的问题发现
     * @param thirdPartyLog
     * @return
     */
    @Async
    public Map gatherV1(ThirdPartyLog thirdPartyLog){
        log.info("服务追踪,接收到的数据为:>{}", thirdPartyLog);
        String url = "http://127.0.0.1:3232/strac/gather/v1";
        Map resData = new HashMap();
        try {
            String resV_ = HttpClientServer.execHttpPost(url, JSONObject.toJSONString(thirdPartyLog));
            resData.put("data", resV_);
            log.info("0000000000000{}", resData);
        } catch (Exception e) {
            e.printStackTrace();
            resData.put("data", e.getMessage());
        }
        return resData;
    }
}
