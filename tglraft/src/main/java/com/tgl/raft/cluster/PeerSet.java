package com.tgl.raft.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: spring-starter
 * @description: 集群节点集合去重、删除操作
 * @author: LIULEI-TGL
 * @create: 2021-05-20 19:46:
 **/
public class PeerSet implements Serializable {

    private PeerSet(){}

    private List<Peer> peerSets = new ArrayList<>();

    /** leader 领导人节点 */
    private volatile Peer leader;

    /** self 自己的节点 */
    private volatile Peer self;

    public static final PeerSet getInstance(){
        return PeerSetLazyHodler.getInstance();
    }

    /**
     * 懒加载PeerSet对象
     * 后期需要优化
     */
    private static class PeerSetLazyHodler{
        private static volatile PeerSet INSTANCE;

        public static final PeerSet getInstance(){
            if (INSTANCE == null){
                synchronized (PeerSetLazyHodler.class){
                    if (INSTANCE == null){
                        INSTANCE = new PeerSet();
                    }
                }
            }
            return INSTANCE;
        }
    }

    /**
     * 添加Peer至list集合中，模拟多节点，多实例
     * @param peer
     */
    public void addPeer(Peer peer){
        peerSets.add(peer);
    }

    public void removePeer(Peer peer){
        peerSets.remove(peer);
    }

    /**
     * 这个操作会把自己self也从集群中误删掉
     * @return
     */
    public List<Peer> getPeerSets() {
        return peerSets;
    }

    /**
     * 把自己从集群节点中删除，并与给其他节点直接发送消息，并不包括自己self
     * TODO 对集群中所有节点的操作，已经把自己排除在外了。所以不用担心会删除自己self
     * @return
     */
    public List<Peer> getPeersWithOutSelf(){
        List tempPeers = new ArrayList(peerSets);
        tempPeers.remove(self);
        return tempPeers;
    }

    public Peer getLeader() {
        return leader;
    }

    public void setLeader(Peer leader) {
        this.leader = leader;
    }

    public Peer getSelf() {
        return self;
    }

    public void setSelf(Peer self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return "PeerSet{" +
                "peerSets=" + peerSets +
                ", leader=" + leader +
                ", self=" + self +
                '}';
    }
}
