package com.tgl.raft.core;

import com.tgl.client.ClientKVAck;
import com.tgl.client.ClientKVReq;
import com.tgl.raft.cluster.NodeConfig;
import com.tgl.raft.entity.AentryParam;
import com.tgl.raft.entity.AentryResult;
import com.tgl.raft.entity.RvoteParam;
import com.tgl.raft.entity.RvoteResult;

/**
 * @program: spring-starter
 * @description: 集群节点设置、设置配置文件、处理请求投票 RPC、处理附加日志数据、处理客户端请求、转发给leader节点
 * @author: LIULEI-TGL
 * @create: 2021-05-20 16:51:
 **/
public interface NodeCore<T> extends LifeCycle {

    /**
     * 设置配置文件.
     * @param nodeConfig
     */
    public void setConfig(NodeConfig nodeConfig);


    /**
     * 处理请求投票RPC
     * @param rvoteParam 投票对象
     * @return
     */
    public RvoteResult handlerRequestRvote(RvoteParam rvoteParam);

    /**
     * 处理附加日志数据
     * @param aentryParam 附加日志数据对象
     * @return
     */
    public AentryResult handlerRequestAentry(AentryParam aentryParam);


    /**
     * 处理客户端请求
     * @param clientKVReq 客户端请求对象
     * @return
     */
    public ClientKVAck handlerClientRequest(ClientKVReq clientKVReq);

    /**
     * 转发给leader节点的消息
     * @param clientKVReq 客户端请求对象
     * @return
     */
    public ClientKVAck redirect(ClientKVReq clientKVReq);
}
