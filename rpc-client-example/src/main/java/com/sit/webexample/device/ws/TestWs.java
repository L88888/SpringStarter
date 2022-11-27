package com.sit.webexample.device.ws;

import com.alipay.remoting.exception.RemotingException;
import com.sailing.rpc.client.BusinessRpcClient;
import com.sailing.rpc.dto.AsyncRequest;
import com.sailing.rpc.dto.ClientKVReq;
import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;
import com.sit.webexample.feignhandler.ServerLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private BusinessRpcClient initRpcClient;

    @Autowired
    private BusinessRpcClient initAsyncRpcClient;

    @Autowired
    private ServerLink serverLink;

    /**
     * curl http://127.0.0.1:12003/v1/testRpc
     * @return
     */
    @RequestMapping(value = "v1/testRpc",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map testRpc() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");

        // RPC服务的访问地址
        // 192.168.137.1
        String nodeAddr = "127.0.0.1:10288";

        Map resquestData = new HashMap();
        resquestData.put("号牌号码", "陕A12345");
        resquestData.put("号牌颜色", "蓝色");
        resquestData.put("号牌种类", "民用");
        resquestData.put("车辆型号", "大众-朗逸");
        // 封装客户端请求对象ClientKVReq,发送请求消息PUT
        ClientKVReq clientKVReq = ClientKVReq.newBuilder()
                .key("陕A1234521")
                .value(resquestData)
                .type(ClientKVReq.PUT)
                .build();

        List temp = new ArrayList(10000);
        for (int i = 0;i < 1000 * 100;i++){
            temp.add(clientKVReq);
        }

        Request request = Request.newBuilder()
                .cmd(Request.R_VOTE)
                .url(nodeAddr)
                .obj(temp)
                .build();

        try {
            // 发送同步消息
            Response<List> response = initRpcClient.send(request);
            log.info("发送同步消息对象RPC====response===value:>{}", response.getResult().size());
        } catch (RemotingException e) {
            e.printStackTrace();
        }

        try {
            // 发送异步消息
            AsyncRequest asyncRequest = new AsyncRequest();
            BeanUtils.copyProperties(request, asyncRequest);
            initAsyncRpcClient.sendAsync(asyncRequest);
            log.info("发送异步消息对象RPC====response===value:>");
        } catch (RemotingException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * curl http://127.0.0.1:12003/v1/testhttp
     * @return
     */
    @RequestMapping(value = "v1/testhttp",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map testHttp() {
        Map map = new HashMap();
        map.put("success", "true");
        map.put("message", "OK");

        String nodeAddr = "127.0.0.1:10288";
        Map resquestData = new HashMap();
        resquestData.put("号牌号码", "陕A12345");
        resquestData.put("号牌颜色", "蓝色");
        resquestData.put("号牌种类", "民用");
        resquestData.put("车辆型号", "大众-朗逸");
        // 封装客户端请求对象ClientKVReq,发送请求消息PUT
        ClientKVReq clientKVReq = ClientKVReq.newBuilder()
                .key("陕A1234521")
                .value(resquestData)
                .type(ClientKVReq.PUT)
                .build();

        List temp = new ArrayList(10000);
        for (int i = 0;i < 1000 * 100;i++){
            temp.add(clientKVReq);
        }

        Request request = Request.newBuilder()
                .cmd(Request.R_VOTE)
                .url(nodeAddr)
                .obj(temp)
                .build();

        try {
            Response<List> response = serverLink.testHttp(request);
            log.info("HTTP====response===value:>{}",response.getResult().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}