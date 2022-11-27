package com.tgl.client;

import com.alipay.remoting.exception.RemotingException;
import com.tgl.raft.concurrent.TglRaftThreadHelper;
import com.tgl.raft.entity.LogEntry;
import com.tgl.raft.rpc.DefaultRaftRpcClient;
import com.tgl.raft.rpc.RaftRpcClient;
import com.tgl.raft.rpc.Request;
import com.tgl.raft.rpc.Response;
import com.tgl.raft.util.RaftUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: spring-starter
 * @description: mock模拟客户端给服务器端发送业务数据消息
 * @author: LIULEI-TGL
 * @create: 2021-06-08 18:57:
 **/
@Slf4j
public class RaftClientLazyInfo {

    // 三个节点分别发送日志消息，然后整个集群查看消息是否已经全部复制
    private final static List<String> nodeList = Arrays.asList("localhost:13141","localhost:13142","localhost:13143");
    // 当前节点信息
    private final static String currentNode = "localhost:13141";

    // 初始化rpc客户端对象
    static RaftRpcClient rpcClient = new DefaultRaftRpcClient();

    public static void main(String[] agre){
        log.info("集群节点集合为:>{}", nodeList);
        testLogMsgReplication();
    }

    /**
     * 测试日志消息发送与节点同步复制
     */
    private static final void testLogMsgReplication(){
//        AtomicInteger index = new AtomicInteger(3);
        try {
            int index = 0;
            for (String nodeAddr : nodeList){
                log.info(nodeAddr);
                index++;
                Map resquestData = new HashMap();
                resquestData.put("号牌号码", "陕A12345");
                resquestData.put("号牌颜色", "蓝色");
                resquestData.put("号牌种类", "民用");
                resquestData.put("车辆型号", "大众-朗逸");
                // 封装客户端请求对象ClientKVReq,发送请求消息PUT
                ClientKVReq clientKVReq = ClientKVReq.newBuilder()
                        .key("陕A1234521" + index)
                        .value(resquestData)
                        .type(ClientKVReq.PUT)
                        .build();

                Request request = Request.newBuilder()
                        .cmd(Request.CLIENT_REQ)
                        .url(nodeAddr)
                        .obj(clientKVReq)
                        .build();

                Response<String> response = rpcClient.send(request);
                log.info("服务器端响应结果为:>{}", response.getResult());

                // 获取服务端响应的结果集对象ClientKVAck
                TglRaftThreadHelper.sleep(1000);

                // 不要吃杏跟桃，娃肚子不舒服
                ClientKVReq clientKVReq1 = ClientKVReq.newBuilder()
                        .type(ClientKVReq.GET)
                        .key("陕A1234521" + index)
                        .build();

                Request getKeyData = Request.newBuilder()
                        .url(nodeAddr)
                        .obj(clientKVReq1)
                        .cmd(Request.CLIENT_REQ)
                        .build();
                Response<LogEntry> response1 = rpcClient.send(getKeyData);
                log.info("获取key:>{},值为:>{}", clientKVReq1.getKey(), response1.getResult().toString());
            }
        } catch (RemotingException e) {
            e.printStackTrace();
        }
    }
}
