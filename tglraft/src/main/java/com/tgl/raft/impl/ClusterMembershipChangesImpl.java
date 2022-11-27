package com.tgl.raft.impl;

import com.tgl.raft.cluster.NodeStatus;
import com.tgl.raft.cluster.Peer;
import com.tgl.raft.entity.LogEntry;
import com.tgl.raft.membership.changes.ClusterMembershipChanges;
import com.tgl.raft.membership.changes.Result;
import com.tgl.raft.rpc.Request;
import com.tgl.raft.rpc.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: spring-starter
 * @description: 处理集群节点变更（新增、删除），线程必须是同步的每次只能新增、删除一个节点
 * @author: LIULEI-TGL
 * @create: 2021-05-23 16:54:
 **/
@Slf4j
public class ClusterMembershipChangesImpl implements ClusterMembershipChanges {

    /**
     * 核心节点
     */
    private final DefaultNodeCoreImpl nodeCore;

    /**
     * 初始化给集群分配默认节点对象
     * @param defaultNodeCore
     */
    public ClusterMembershipChangesImpl(DefaultNodeCoreImpl defaultNodeCore){
        this.nodeCore = defaultNodeCore;
    }

    /** 新增锁 */
    private ReentrantLock addLock = new ReentrantLock();
    /** 删除锁 */
    private ReentrantLock removeLock = new ReentrantLock();

    /**
     * 增加核心节点, 每次只能增加一个核心节点，牵扯到日志拷贝、状态机数据复制、集群中其他节点同步新加节点对象
     * @param newPeer 新加节点Peer
     * @return
     */
    @Override
    public Result addPeer(Peer newPeer) {
        try {
            if (addLock.tryLock(1000, TimeUnit.MILLISECONDS)){
                if (nodeCore.getPeerSet().getPeersWithOutSelf().contains(newPeer)){
                    return new Result(0,"集群中已存在节点对象:>>>" + newPeer.getAddr());
                }
                // 将节点添加至集群中
                nodeCore.getPeerSet().getPeersWithOutSelf().add(newPeer);

                // 只有leader节点才可以处理新加的node peer,处理新加的节点对象
                if (nodeCore.getState() == NodeStatus.LEADER){
                    nodeCore.getNextIndexs().put(newPeer, 0L);
                    nodeCore.getMatchIndexs().put(newPeer, 0L);

                    // 复制leader的日志给新加的集群节点
                    log.info("开始复制leader节点的日志信息");
                    LogEntry logEntry;
                    for (long i = 0; i < nodeCore.getLogModule().getLastIndex(); i++){
                        logEntry = nodeCore.getLogModule().read(i);
                        // 开始复制leader的日志对象
                        if (logEntry != null){
                            // 开启多线程并行复制leader的日志给新节点
                            nodeCore.replication(newPeer, logEntry);
                        }
                    }
                    log.info("已结束复制leader节点的日志信息");

                    // 请求对象
                    Request request;
                    // 响应对象
                    Response response;
                    // 结果集对象
                    Result result;
                    // 把新节点的信息同步给集群中其他的节点
                    for (Peer itemPeer : nodeCore.getPeerSet().getPeersWithOutSelf()){
                        // 封装请求对象
                        request = Request.newBuilder()
                                .cmd(Request.CHANGE_CONFIG_ADD)
                                .url(newPeer.getAddr())
                                .obj(newPeer)
                                .build();
                        // 给其他的节点发送新增节点的对象
                        response = nodeCore.getRaftRpcClient().send(request);
                        result = (Result) response.getResult();
                        // 判断消息处理的结果Result对象
                        if (result != null && result.getStatus() == Result.ResultStatusEnum.SUCCESS.getCodeVal()){
                            log.info("新节点{}同步至集群节点{}成功.", newPeer, itemPeer);
                        }else {
                            log.info("新节点{}同步至集群节点{}失败.", newPeer, itemPeer);
                        }
                    }
                }
            }else {
                return new Result(0,"添加集群节点未获取到锁信息.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加集群节点异常，异常信息为:>>>{}", e.fillInStackTrace());
        } finally {
            addLock.unlock();
        }
        return new Result(0,"RAFT集群添加节点失败.");
    }

    /**
     * 从集群中删除一个原有节点对象，每次只能删除一个节点
     * @param oldPeer 原有节点Peer
     * @return
     */
    @Override
    public Result removePeer(Peer oldPeer) {
        try {
            if (removeLock.tryLock(1000, TimeUnit.MILLISECONDS)){
                // 删除非自身的Node对象
                nodeCore.getPeerSet().getPeersWithOutSelf().remove(oldPeer);
                nodeCore.getNextIndexs().remove(oldPeer);
                nodeCore.getMatchIndexs().remove(oldPeer);
                return new Result(1,"删除节点" + oldPeer.getAddr() + "成功.");
            }else {
                return new Result(0,"删除集群节点未获取到锁信息.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("删除集群节点异常，异常信息为:>>>{}", e.getStackTrace());
        }finally {
            removeLock.unlock();
        }

        return new Result(0,"删除集群节点失败.");
    }
}
