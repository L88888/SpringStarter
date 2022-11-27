package com.tgl.application;

import com.alipay.remoting.util.StringUtils;
//import com.google.common.collect.Lists;
import com.tgl.raft.cluster.NodeConfig;
import com.tgl.raft.core.NodeCore;
import com.tgl.raft.impl.DefaultNodeCoreImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: spring-starter
 * @description: 初始化集群核心配置
 * @author: LIULEI-TGL
 * @create: 2021-06-08 17:59:
 **/
@Slf4j
public class TglRaftNodeBootStarp {

    /**
     * 守护主线程配置节点基本信息
     * 启动程序时带入系统入参
     * -D serverPort=13141
     * -D serverPort=13142
     * -D serverPort=13143
     * -D serverPort=13144
     * -D serverPort=13145
     */
    private static void daemon(int selfPort){
        // 设置集群节点信息(mock)
//        List peerAddr = Lists.newArrayList("localhost:13141","localhost:13142"
//                ,"localhost:13143","localhost:13144","localhost:13145");

        List peerAddr = null;

        NodeConfig nodeConfig = new NodeConfig();
        // 设置自身节点端口
        nodeConfig.setSelfPort(selfPort);
        // 设置其他节点地址
        nodeConfig.setPerAddrs(peerAddr);

        try {
            // 配置节点Node，开始启动RPC服务
            NodeCore nodeCore = DefaultNodeCoreImpl.getInstance();
            // 配置集群
            nodeCore.setConfig(nodeConfig);
            // 初始化per node 中的一致性、集群、选举、心跳、失败日志重试、最新的日志条目
            nodeCore.init();

            // 停机前销毁RPC服务
            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                try {
                    nodeCore.destroy();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    log.warn("Raft 服务端停止失败.在停止一次.");
                    try {
                        nodeCore.destroy();
                    } catch (Throwable throwable1) {
                        throwable1.printStackTrace();
                    }
                }
            }));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.warn("Raft 服务端启动失败.");
        }
    }

    public static void main(String[] ager){
        String selfPort = System.getProperty("serverPort");
        if (StringUtils.isBlank(selfPort)){
            log.info("未检测到端口号，程序自动退出。");
            System.exit(0);
        }

        try {
            int selfPort1 = Integer.parseInt(selfPort);
            daemon(selfPort1);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
