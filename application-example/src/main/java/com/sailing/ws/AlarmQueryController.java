package com.sailing.ws;

import com.sailing.service.elasticclient.AlarmQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: spring-starter
 * @description: 人脸信息管理
 * @author: LIULEI
 * @create: 2020-08-06 15:26:
 **/
@RestController
public class AlarmQueryController {

    @Autowired
    private AlarmQuery alarmQuery;

    /**
     * 通过springboot-starter的方式查询人脸预警数据
     * curl http://127.0.0.1:8080/queryAlarmData/v1
     * @return
     */
    @RequestMapping(value = "queryAlarmData/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map queryAlarmData() {
        return alarmQuery.queryAlarmData();
    }
}
