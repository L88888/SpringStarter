package com.tgl.rdbms.ws;

import com.tgl.rdbms.core.VehicleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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
public class TestWs {

    @Autowired
    private VehicleServiceImpl vehicleService;

    /**
     * 测试批量数据提交入库clickhouse
     * curl http://127.0.0.1:2002/batch/data/v1
     * @return
     */
    @RequestMapping(value = "batch/data/v1",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map check() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        vehicleService.insertVehicleinfo();
        return map;
    }
}
