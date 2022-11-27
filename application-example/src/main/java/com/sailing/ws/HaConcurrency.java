package com.sailing.ws;

import com.sailing.service.highconcurrency.Identification;
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
 * @description: 测试Springboot 封装的线程池用于
 * @author: LIULEI
 * @create: 2020-08-18 16:52:
 **/
@RestController
public class HaConcurrency {

    @Autowired
    private Identification identification;

    /**
     * http://127.0.0.1:8080/_identification/v1
     * @return
     */
    @RequestMapping(value = "_identification/v1",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map _identification(){
        int pi = 1;
        identification.initDataGather(pi);

        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }
}
