package com.sailing.rpc.bootstart;

import com.sailing.rpc.server.InitRpcServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: spring-starter
 * @description: 初始化集群核心配置
 * @author: LIULEI-TGL
 * @create: 2021-06-08 17:59:
 **/
@Slf4j
public class NodeBootStarp {

    /**
     * 守护主线程配置节点基本信息
     * 启动程序时带入系统入参
     * -D serverPort=10288
     */
    public static void daemon(int selfPort){
        try {
            log.info("初始化RPC服务端节点:>{}", selfPort);
            // 设置集群节点信息(mock)
            // 配置节点Node，开始启动RPC服务
            InitRpcServer defaultRaftRpcServer = new InitRpcServer(selfPort);
            log.info("初始化RPC服务端节点完成.");
            defaultRaftRpcServer.start();

            // 停机前销毁RPC服务
            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                log.info("注销RPC服务端节点:>{}", selfPort);
                try {
                    defaultRaftRpcServer.stop();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    log.warn("Raft 服务端停止失败.在停止一次.");
                    try {
                        defaultRaftRpcServer.stop();
                    } catch (Throwable throwable1) {
                        throwable1.printStackTrace();
                    }
                }
                log.info("注销RPC服务端节点完成.");
            }));
        } catch (Throwable throwable) {
            log.debug("Raft 服务端启动失败:>{}", throwable.fillInStackTrace());
        }
    }
}
