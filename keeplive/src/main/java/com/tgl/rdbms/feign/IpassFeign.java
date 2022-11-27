package com.tgl.rdbms.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;

@Component
@FeignClient(name = "server-ipass",
        url = "${ipass_service_url}")
public interface IpassFeign {

    /**
     *  注销登录的用户对象
     *  curl -X GET "http://127.0.0.1:9200/external-interface/ajaxLogout" -H "accept: application/json" -H "Authorization: 123"
     * @return
     */
    @RequestMapping(value = "/ajaxLogout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map ajaxLogout(@RequestHeader(name = "Authorization", required = true) String authorization)throws Exception;
}
