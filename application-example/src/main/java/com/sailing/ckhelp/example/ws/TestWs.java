package com.sailing.ckhelp.example.ws;

import com.sailing.ckhelp.example.server.MetricsDataProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-27 18:29:
 **/
@RestController
@Slf4j
public class TestWs {

    @Autowired
    private MetricsDataProcess metricsDataProcess;

    /**
     * 测试批量数据提交入库clickhouse
     * curl http://127.0.0.1:20222/batch/data/v1
     * @return
     */
    @RequestMapping(value = "batch/data/v1",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map check() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", metricsDataProcess.dataStatistics());

        return map;
    }
}
