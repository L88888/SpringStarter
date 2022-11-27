package com.tgl.raft.membership.changes;

import com.tgl.raft.cluster.Peer;

/**
 * @program: spring-starter
 * @description: 集群节点新增与删除
 * @author: LIULEI-TGL
 * @create: 2021-05-20 13:03:
 **/
public interface ClusterMembershipChanges {

    /**
     * 给集群中添加节点对象Peer
     * @param newPeer 新加节点Peer
     * @return
     */
    public Result addPeer(Peer newPeer);

    /**
     * 从集群中删除节点对象Peer
     * @param oldPeer 原有节点Peer
     * @return
     */
    public Result removePeer(Peer oldPeer);
}
