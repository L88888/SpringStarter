package com.sailing.reflex.highconcurrency;

import com.sailing.application.ExampleApplication;
import com.sailing.linkstrack.ServiceTrackData;
import com.sailing.linkstrack.bo.ThirdPartyLog;
import com.sailing.service.highconcurrency.Identification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2020-08-24 14:44:
 **/
@Slf4j
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = ExampleApplication.class)
public class HighConsurrencyTest {

    @Autowired
    private Identification identification;

    @Autowired
    private ServiceTrackData serviceTrackData;

    /**
     * 并发一次多个请求测试
     */
    @Test
    public void _identification() {
        int pi = 1;
        identification.initDataGather(pi);
    }

    /**
     * 单个请求直接测试
     */
    @Test
    public void serviceTrackTest(){
        int id = 1;
        ThirdPartyLog thirdPartyLog = new ThirdPartyLog();
        thirdPartyLog.setName("测试接口A00" + id);
        thirdPartyLog.setMethod("POST");
        thirdPartyLog.setUrl("http://127.0.0.1:3232/strac/gather/v1");
        thirdPartyLog.setParams("ab=1");
        thirdPartyLog.setVendor("熙菱");
        thirdPartyLog.setDescription("测试接口A00" + id + ";高并发场景下的sqlite锁表问题浮现");
        thirdPartyLog.setResult("ab=1");
        thirdPartyLog.setUserCode("user07");
        log.info("请求对象报文为:>{}",thirdPartyLog);

        for (int i =0;i< 10;i++){
            Map res = serviceTrackData.gatherV1(thirdPartyLog);
            log.info("res:>{}",res);
        }
    }
}
