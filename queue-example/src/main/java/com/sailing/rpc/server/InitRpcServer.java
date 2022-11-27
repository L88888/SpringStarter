package com.sailing.rpc.server;

import com.alipay.remoting.rpc.RpcServer;
import com.alipay.remoting.rpc.protocol.AbstractUserProcessor;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.sailing.rpc.dto.AsyncRequest;
import com.sailing.rpc.dto.Request;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: spring-starter
 * @description: 实现采用Alipay netty tcp 协议组件；接收客户端发送的消息对象，并调用默认节点（DefaultNodeCoreImpl）处理消息对象各种动作;
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:57:
 **/
@Slf4j
public class InitRpcServer implements BusinessRpcServer {

    /**
     * rpcserver 对象声明
     */
    private static RpcServer rpcServer;

    /**
     * 初始化创建RpcServer服务端
     * 定义同步(BusinessProcessor)与异步(BusinessAsyncProcessor)消息处理对象
     * @param port
     */
    public InitRpcServer(int port){
        rpcServer = new RpcServer(port, false, false);
        // 添加用户自定义同步接口处理对象
        AbstractUserProcessor raftUserProcessor = new BusinessProcessor<Request>();
        rpcServer.registerUserProcessor(raftUserProcessor);

        // 添加用户自定义异步接口处理对象
        AsyncUserProcessor asyncUserProcessor = new BusinessAsyncProcessor<AsyncRequest>();
        rpcServer.registerUserProcessor(asyncUserProcessor);
    }

    @Override
    public void stop() {
        rpcServer.shutdown();
    }

    public static void closeService(){
        rpcServer.shutdown();
    }

    @Override
    public void start() {
        rpcServer.startup();
    }
}
