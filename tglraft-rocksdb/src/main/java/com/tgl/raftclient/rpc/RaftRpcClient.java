package com.tgl.raftclient.rpc;

import com.alipay.remoting.exception.RemotingException;

/**
 * @program: spring-starter
 * @description: RPC客户端只给RPC服务端发送消息请求
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:46:
 **/
public interface RaftRpcClient {

    /**
     * 发送消息数据对象
     * @param requestData 消息对象
     * @return
     */
    Response send(Request requestData)throws RemotingException;

    /**
     * 发送消息数据对象，带超时时间
     * @param requestData 消息对象
     * @param timeOut 超时时间
     * @return
     */
    Response send(Request requestData, int timeOut)throws RemotingException;
}
