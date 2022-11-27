package com.sailing.ws;

import com.sailing.service.production.ProdectBussDataMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: es-spring-boot-starter-pom
 * @description: RocketMQ实例
 * @author: LIULEI
 * @create: 2020-07-24 15:35:
 **/
@RestController
public class ProdectDataMQController {

    @Autowired
    private ProdectBussDataMQ prodectBussDataMQ;

    /**
     * 生产业务数据至RocketMQ
     * curl http://127.0.0.1:8080/addData/v1
     * @return
     */
    @RequestMapping(value = "addData/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map addData() {
        String tagName = "face-001";
//        tagName = "face-002";
        String key = "face-001";
        prodectBussDataMQ.prodectBussData(tagName, key);
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }
}
