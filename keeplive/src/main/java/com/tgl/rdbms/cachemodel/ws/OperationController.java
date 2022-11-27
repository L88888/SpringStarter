package com.tgl.rdbms.cachemodel.ws;

import com.tgl.rdbms.cachemodel.entity.LogEntry;
import com.tgl.rdbms.cachemodel.entity.ThirdPartyLog;
import com.tgl.rdbms.cachemodel.service.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring-starter
 * @description: 操作类给缓存中写入数据
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-02 11:10:
 **/
@RestController
@Slf4j
public class OperationController {

    @Autowired
    private OperationService operationService;

    /**
     * 测试服务日志采集接口
     * http://127.0.0.1:2003/gather/v12
     * @return
     */
    @RequestMapping(value = "/gather/v12",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map gatherBussLog(@RequestBody ThirdPartyLog thirdPartyLog) {
        log.info("待入库的日志数据为:>{}", thirdPartyLog);
        operationService.logDataWrite(thirdPartyLog);

        Map resultData = new HashMap();
        resultData.put("stats","服务日志数据存储成功.");
        return resultData;
    }

    /**
     * 测试服务日志采集接口,获取日志数据
     * curl http://127.0.0.1:2003/gather/getdata/v12/1L
     * @return
     */
    @RequestMapping(value = "gather/getdata/v12/{index}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map queryAlarmData(@PathVariable("index") String id) {
        LogEntry resData = operationService.getLogData(id);
        Map resultData = new HashMap();
        resultData.put("stats","获取日志数据成功.");
        resultData.put("data",resData);
        return resultData;
    }
}
