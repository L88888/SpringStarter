package com.sailing.tgl.tests;

import com.alipay.remoting.exception.RemotingException;
import com.sailing.rpc.client.InitRpcClient;
import com.sailing.rpc.client.BusinessRpcClient;
import com.sailing.rpc.dto.ClientKVReq;
import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;

import java.util.HashMap;
import java.util.Map;

public class T1 {

    private static BusinessRpcClient raftRpcClient = new InitRpcClient();

    public static void main(String[] arge) {
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

        Request request = Request.newBuilder()
                .cmd(Request.R_VOTE)
                .url(nodeAddr)
                .obj(clientKVReq)
                .build();

        try {
            Response<String> response = raftRpcClient.send(request);
            System.out.println("RPC====response===value:>{}" + response);
        } catch (RemotingException e) {
            e.printStackTrace();
        }
    }
}