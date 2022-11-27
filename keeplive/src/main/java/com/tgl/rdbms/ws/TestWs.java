package com.tgl.rdbms.ws;

import com.tgl.rdbms.service.DownloadImages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private DownloadImages downloadImagesImpl;

    /**
     * 测试批量数据提交入库clickhouse
     * curl http://127.0.0.1:2003/batch/data/v1
     * @return
     */
    @RequestMapping(value = "batch/data/v1",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map check() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
//        vehicleService.insertVehicleinfo();
        return map;
    }

    /**
     * 注销登录的用户对象
     * curl -X GET "http://127.0.0.1:2003/external-interface/ajaxLogout" -H "accept: application/json" -H "Authorization: 123"
     * @param authorization
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "external-interface/ajaxLogout",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map ajaxLogout(@RequestHeader(name = "Authorization", required = true) String authorization)throws Exception {
        log.info("注销用户的token认证信息为:>{}", authorization);
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");
//        vehicleService.insertVehicleinfo();
        return map;
    }

    /**
     * 获取批量下载图片
     * http://127.0.0.1:2003/v1/downloadImages/data
     * @return
     */
    @RequestMapping(value = "v1/downloadImages/data",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map downloadImages() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");

        downloadImagesImpl.getImages();
        return map;
    }
}
