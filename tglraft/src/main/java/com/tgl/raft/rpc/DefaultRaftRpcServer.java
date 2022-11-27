package com.tgl.raft.rpc;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.RpcServer;
import com.tgl.client.ClientKVReq;
import com.tgl.raft.cluster.Peer;
import com.tgl.raft.entity.AentryParam;
import com.tgl.raft.entity.RvoteParam;
import com.tgl.raft.impl.DefaultNodeCoreImpl;
import com.tgl.raft.membership.changes.ClusterMembershipChanges;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: spring-starter
 * @description: 实现采用Alipay netty tcp 协议组件；接收客户端发送的消息对象，并调用默认节点（DefaultNodeCoreImpl）处理消息对象各种动作;
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:57:
 **/
@Slf4j
public class DefaultRaftRpcServer implements RaftRpcServer {

    /** 保证rcpserver 与 DefaultNodeCore对象只被初始化一次 */
    private volatile boolean flag;

    /** rpcserver 对象声明 */
    private RpcServer rpcServer;

    /** 默认处理节点 */
    private DefaultNodeCoreImpl defaultNodeCore;

    /** 每次只能有一个线程在创建 rpcserver 与 defaultNodeCore对象 */
    private ReentrantLock reentrantLock = new ReentrantLock();

    public DefaultRaftRpcServer(int port, DefaultNodeCoreImpl defaultNodeCore){
        if (flag){
            return;
        }

        try {
            // 先获取到锁,然后在进行业务操作
            if (reentrantLock.tryLock()){
                if (flag){
                    return;
                }

                rpcServer = new RpcServer(port, false, false);
                // 添加用户自定义接口处理对象
                rpcServer.registerUserProcessor(new RaftUserProcessor<Request>() {
                    @Override
                    public Object handleRequest(BizContext bizCtx, Request request) throws Exception {
                        return proHandlerRequest(request);
                    }
                });
                this.defaultNodeCore = defaultNodeCore;
                this.flag = true;
            }else{
                log.info("未获取到锁对象.");
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override
    public void stop() {
        rpcServer.stop();
    }

    @Override
    public void start() {
        rpcServer.start();
    }

    /**
     * 处理来自客户端的请求cmd命令,主要有默认节点进行处理调度
     * @param request
     * @return
     */
    @Override
    public Response proHandlerRequest(Request request) {
        if (request == null){
            log.info("请求对象为null，服务器端无法处理.");
            return Response.fail("请求对象为null，服务器端无法处理.");
        }

        // 统一返回处理结果对象
        Response response = null;
        // 判断cmd处理类型
        switch(request.getCmd()){
            case Request.R_VOTE:
                // 处理投票请求
                response = new Response(defaultNodeCore.handlerRequestRvote((RvoteParam) request.getObj()));
                break;
            case Request.A_ENTRY:
                // 处理日志附加请求
                response = new Response(defaultNodeCore.handlerRequestAentry((AentryParam) request.getObj()));
                break;
            case Request.CLIENT_REQ:
                // 处理客户端消息请求
                response = new Response(defaultNodeCore.handlerClientRequest((ClientKVReq) request.getObj()));
                break;
            case Request.CHANGE_CONFIG_ADD:
                // 添加集群节点
                response = new Response(((ClusterMembershipChanges)defaultNodeCore).addPeer((Peer) request.getObj()));
                break;
            case Request.CHANGE_CONFIG_REMOVE:
                // 移除汲取节点
                response = new Response(((ClusterMembershipChanges)defaultNodeCore).removePeer((Peer) request.getObj()));
                break;
        }
        return response;
    }
}
