package com.sailing.rpc.client;

import com.alipay.remoting.exception.RemotingException;
import com.sailing.rpc.dto.AsyncRequest;
import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;

/**
 * @program: spring-starter
 * @description: RPC客户端只给RPC服务端发送消息请求
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:46:
 **/
public interface BusinessRpcClient {

    /**
     * 发送消息数据对象，默认超时时间5秒
     * @param requestData 消息对象
     * @return
     */
    Response send(Request requestData)throws RemotingException;

    /**
     * 发送消息数据对象，超时时间可自定义默认5秒
     * @param requestData 消息对象
     * @param timeOut 超时时间
     * @return
     */
    Response send(Request requestData, int timeOut)throws RemotingException;

    /**
     * 异步消息请求，默认超时时间5秒
     * @param requestData
     * @throws RemotingException
     */
    void sendAsync(AsyncRequest requestData)throws RemotingException;

    /**
     * 异步消息请求，超时时间可自定义默认5秒
     * @param requestData
     * @param timeOut
     * @throws RemotingException
     */
    void sendAsync(AsyncRequest requestData, int timeOut)throws RemotingException;
}
