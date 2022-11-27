package com.tgl.raft.rpc;

/**
 * @program: spring-starter
 * @description: RPC服务端负责处理客户端的所有消息数据
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:46:
 **/
public interface RaftRpcServer {

    /**
     * 停止RPC服务端
     */
    void stop();

    /**
     * 启动RPC服务端
     */
    void start();

    /**
     * 处理客户端请求
     * @param request
     * @return
     */
    Response proHandlerRequest(Request request);
}
