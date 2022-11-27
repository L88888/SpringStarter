package com.sailing.ws;

import com.sailing.feignhandler.client.Sabnormal;
import com.sailing.linkstrack.bo.ThirdPartyLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @program: spring-starter
 * @description: 测试feign500状态，异常信息捕获
 * @author: LIULEI
 * @create: 2020-08-15 23:20:
 **/
@Slf4j
@RestController
public class FeignAbnormalExample {

    @Autowired
    private Sabnormal sabnormal;

    /**
     * curl http://127.0.0.1:20210/getalarmdata/v1
     * http://127.0.0.1:20210/getalarmdata/v1
     * @return
     */
    @RequestMapping(value = "getalarmdata/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map getAlarmData() {
        Map resultData = new HashMap();
        resultData.put("data","test one success.");
        Map catData = sabnormal.queryCatAlarmData();

        log.info("0000000000{}",catData);
        return resultData;
    }


    /**
     * http://127.0.0.1:20210/querycatalarmdata/v1
     * @return
     */
    @RequestMapping(value = "querycatalarmdata/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map queryCatAlarmData() {
        Map resultData = new HashMap();
        resultData.put("data","test one querycatalarmdata full.");

        boolean forkLiand = true;
        if (forkLiand){
            Integer.parseInt("s");
            throw new RuntimeException("数据类型转换异常.");
        }
        return resultData;
    }

    /**
     * curl http://127.0.0.1:20210/addbusslogdata/v1
     * http://127.0.0.1:20210/addbusslogdata/v1
     * @return
     */
    @RequestMapping(value = "addbusslogdata/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map addBussLogData() {
        Map resultData = new HashMap();
        resultData.put("data","test one success.");

        ThirdPartyLog thirdPartyLog = new ThirdPartyLog();
        thirdPartyLog.setId(UUID.randomUUID().toString());
        thirdPartyLog.setVendor("特工刘");
        thirdPartyLog.setUserCode("tegongliu");
        thirdPartyLog.setUrl("addbusslogdata/v1");
        thirdPartyLog.setMethod("addBussLogData");
        thirdPartyLog.setResult("获取API接口请求参数成功。");
        thirdPartyLog.setParams("wwww");
        thirdPartyLog.setOptTime(new Date().toString());
        thirdPartyLog.setStatus(true);
        Map catData = sabnormal.gatherV1(thirdPartyLog);

        log.info("0000000000{}",catData);
        return resultData;
    }

    /**
     * 测试服务日志采集接口
     * http://127.0.0.1:20210/gather/v1
     * @return
     */
    @RequestMapping(value = "/gather/v1",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map gatherBussLog(@RequestBody ThirdPartyLog thirdPartyLog) {
        log.info("待入库的日志数据为:>{}", thirdPartyLog);
        Map resultData = new HashMap();
        resultData.put("data","服务日志数据存储成功.");
        return resultData;
    }

    /**
     * 测试服务日志采集接口
     * http://127.0.0.1:20210/gather/v2
     * @return
     */
    @RequestMapping(value = "/gather/v2",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map gatherBussLog2(@RequestBody ThirdPartyLog thirdPartyLog) {
        log.info("待入库的日志数据为:>{}", thirdPartyLog);
        Map resultData = new HashMap();
        resultData.put("data","服务日志数据存储成功.");
        return resultData;
    }
}
