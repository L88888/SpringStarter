package com.sit.client.buss.ws;

import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class TestLink {

    /**
     * curl http://127.0.0.1:12002/v1/testrequest
     * @return
     */
    @RequestMapping(value = "v1/testrequest",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Response testHttp(@RequestBody Request dataParam) {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");


        log.info("HTTP客户端请求报文:>{}", dataParam.getUrl());
        if (dataParam == null){
            log.info("请求对象为null，服务器端无法处理.");
            return Response.fail("请求对象为null，服务器端无法处理.");
        }

        // 统一返回处理结果对象
        Response response = null;
        // 判断cmd处理类型
        switch(dataParam.getCmd()){
            case Request.R_VOTE:
                // 处理投票请求
                response = new Response(dataParam.getObj());
                break;
        }
        return response;
    }
}
