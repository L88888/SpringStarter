package com.sailing.tgl.test;

import com.tgl.client.ClientKVReq;
import com.tgl.raft.cluster.NodeStatus;
import com.tgl.raft.cluster.Peer;
import com.tgl.raft.cluster.PeerSet;
import com.tgl.raft.entity.*;
import com.tgl.raft.membership.changes.Result;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-05-19 15:59:
 **/
public class FianlTest {

    @Test
    public void testPeer(){
        Peer peer = new Peer("127.0.0.180:8091");
        System.out.println(peer.hashCode() + peer.toString());

        Peer peer1 = new Peer("192.168.80.20:8089");
        System.out.println(peer1.hashCode() + peer1.toString());
    }

    @Test
    public void testBuilder(){
        // key/value存储命令
        Command command = Command.newBuilder()
                .key("acml-001")
                .value("测试00900.")
                .build();
        System.out.println("command is value data:>>>" + command.toString());

        /** 待写入的日志条目 */
        LogEntry logEntry = LogEntry.newBuilder()
                .index(123l)
                .term(11111l)
                .command(command)
                .build();
        System.out.println("logEntry is value data:>>>" + logEntry.toString());

        /** 候选人的投票对象 */
        RvoteParam rvoteParam = RvoteParam.newBuilder()
                .candidateId("127.0.0.1:12345")
                .lastLogIndex(123123L)
                .lastLogTerm(2134L)
                .serviceId("127.0.0.1S1")
                .term(123L)
                .build();
        System.out.println("rvoteParam is value data:>>>" + rvoteParam.toString());

        /** 候选人的投票结果 */
        RvoteResult rvoteResult = RvoteResult.newBuilder()
                .term(33333l)
                .voteGranted(1==3)
                .build();
        System.out.println("rvoteresult is value data:>>>" + rvoteResult.toString());

        /** 附加日志请求对象，由客户端发起给服务器端 */
        AentryParam aentryParam = AentryParam.newBuilder()
                .term(1111l)
                .keepLive(true)
                .leaderCommit(123l)
                .leaderId("myleader")
                .logEntrys((LogEntry[]) Arrays.asList(logEntry).toArray())
                .prevLogIndex(333l)
                .prevLogTerm(456l)
                .serviceId("myleader S1009")
                .build();
        System.out.println("aentryParam is value data:>>>" + aentryParam.toString());

        /** 附加日志的结果，有服务器端处理完后返回 */
        AentryResult aentryResult = AentryResult.newBuilder()
                .term(123l)
                .success(false)
                .build();
        System.out.println("aentryResult is value data:>>>" + aentryResult);
    }

    @Test
    public void testEnum(){
        int val = 312;
        String name = Result.ResultStatusEnum.enumValue(val).toString();
        System.out.println(name);

        name = NodeStatus.NodeStatusEnum.CANDIDDATE.toString();
        System.out.println(name);
        name = NodeStatus.NodeStatusEnum.enumValue(2).toString();
        System.out.println(name);
    }

    @Test
    public void operationClusterNode(){
        PeerSet peerSet = PeerSet.getInstance();

        // 本节点
        Peer selfPeer = new Peer("127.0.0.1");
        // leader节点
        Peer leaderPeer = new Peer("127.0.0.5");
        Peer peer1 = new Peer("127.0.0.2");
        Peer peer2 = new Peer("127.0.0.3");
        Peer peer3 = new Peer("127.0.0.4");
        peerSet.addPeer(selfPeer);
        peerSet.addPeer(peer1);
        peerSet.addPeer(peer2);
        peerSet.addPeer(peer3);
        System.out.println("未设置self节点:>>" + peerSet.getPeersWithOutSelf());

        // 自己self
        peerSet.setSelf(selfPeer);
        // leader
        peerSet.setLeader(leaderPeer);
        // 输出非自己以外的所有节点
        System.out.println("设置了self节点:>>" + peerSet.getPeersWithOutSelf());

        System.out.println("获取leader节点对象:>>" + peerSet.getLeader());

        String printTable = ClassLayout.parseInstance(peerSet).toPrintable();
        System.out.println(printTable);
    }

    @Test
    public void testResult(){
        Result.Builder result = Result.newBuilder();
        Result rst = result.leaderHint("成功").status(1000).build();
        System.out.println("返回对象结果集EnumValue is name :>>" + Result.ResultStatusEnum.enumValue(1).name());
        System.out.println("返回对象结果集EnumValue is name :>>" + Result.ResultStatusEnum.SUCCESS);
        System.out.println("返回对象结果集EnumValue is 获取枚举值 :>>" + Result.ResultStatusEnum.SUCCESS.getCodeVal());
        System.out.println("返回对象结果集EnumValue is 获取枚举值 :>>" + Result.ResultStatusEnum.FAIL.getCodeVal());
        System.out.println("返回对象结果集EnumValue is 获取枚举值 :>>" + Result.ResultStatusEnum.INVALID.getCodeVal());
        System.out.println("返回对象结果集EnumValue is code :>>" + Result.ResultStatusEnum.SBBH);
        System.out.println("返回对象结果集EnumValue is enumCode :>>" + Result.ResultStatusEnum.SBBH.getEnumCode());
        System.out.println("返回对象结果集EnumValue is name :>>" + Result.ResultStatusEnum.SBBH.name());
        System.out.println("返回对象结果集:>>" + rst);

        System.out.println("返回对象结果集clientRequest:>>" + ClientKVReq.ReqType.enumValue(1));
        System.out.println("返回对象结果集当前节点状态:>>" + NodeStatus.NodeStatusEnum.enumValue(NodeStatus.FOLLOWER));
    }

    @Test
    public void testThreadLocalRandom(){
        System.out.println(ThreadLocalRandom.current().nextInt(50));
    }


}

