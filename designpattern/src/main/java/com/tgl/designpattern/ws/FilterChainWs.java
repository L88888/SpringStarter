package com.tgl.designpattern.ws;

import com.tgl.designpattern.service.responsibilitychain.inherit.MyParams;
import com.tgl.designpattern.service.responsibilitychain.inherit.PrepareFilters;
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
public class FilterChainWs {

    @Autowired
    private PrepareFilters autoPrepareFilter;

    /**
     * curl http://127.0.0.1:2003/filter/v2
     * @return
     */
    @RequestMapping(value = "filter/v2",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map _identification(){
        MyParams myParam = new MyParams();
        myParam.setGmsfhm("610423123456789");
        myParam.setXm("张三");
        myParam.setXb("女");
        myParam.setCsrq("1977-11-13");
        autoPrepareFilter.doFilter(myParam);

        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
        return map;
    }
}
