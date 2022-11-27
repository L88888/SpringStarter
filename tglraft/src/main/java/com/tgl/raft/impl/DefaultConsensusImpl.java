package com.tgl.raft.impl;

import com.alipay.remoting.util.StringUtils;
import com.tgl.raft.cluster.NodeStatus;
import com.tgl.raft.cluster.Peer;
import com.tgl.raft.core.Consensus;
import com.tgl.raft.entity.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: spring-starter
 * @description: 一致性实现机制（投票、日志附加）
 * @author: LIULEI-TGL
 * @create: 2021-05-23 16:57:
 **/
@Slf4j
public class DefaultConsensusImpl implements Consensus {

    private DefaultNodeCoreImpl nodeCore;

    public DefaultConsensusImpl(DefaultNodeCoreImpl defaultNodeCore){
        this.nodeCore = defaultNodeCore;
    }

    // 创建两把锁
    private ReentrantLock voteLock = new ReentrantLock();
    private ReentrantLock aentryLock = new ReentrantLock();

    /**
     * 接收者实现
     *  一、如果任期小于当前任期返回false，term < currentTerm return false
     *   二、如果 votedFor （为投票）为空或者就是 candidateId，并且候选人的日志至少和自己的一样新，那么就投票给他
     * @param rvoteParam 候选人投票参数对象，任期与日志索引必须要跟leader保持一致
     * @return
     */
    @Override
    public RvoteResult requestVote(RvoteParam rvoteParam) {
        RvoteResult.Builder rvoteResult = RvoteResult.newBuilder();
        try {
            // 先得到锁在开始参与投票
            if (voteLock.tryLock()){
                // Step1 投票人的任期没有自身的大
                if (rvoteParam.getTerm() < nodeCore.getCurrentTerm()){
                    log.info("候选人的任期没有自己大，不给候选人投票。");
                    return rvoteResult.term(nodeCore.getCurrentTerm()).voteGranted(false).build();
                }

                // 输出候选人的任期、候选人ID
                log.info("候选人对象:{}", rvoteParam);
                log.info("当前节点对象:{},当前获得选票的候选人ID:{},候选人ID:{}",
                        nodeCore.getPeerSet().getSelf(), nodeCore.getVotedForId(), rvoteParam.getCandidateId());
                log.info("当前节点对象:{},当前节点任期:{},候选人任期:{}",
                        nodeCore.getPeerSet().getSelf(), nodeCore.getCurrentTerm(), rvoteParam.getTerm());

                // Step2 当前节点没有投票 或者以及投票过了且是对方节点 && 对方节点的日志与自己一样新，投票给对方
                if (StringUtils.isEmpty(nodeCore.getVotedForId()) ||
                        nodeCore.getVotedForId().equals(rvoteParam.getCandidateId())){
                    // 判断日志是否与自己一致，如果一致就投票给对方
                    if (nodeCore.getLogModule().getLast() != null){
                        // 判断日志条目任期
                        if (nodeCore.getLogModule().getLast().getTerm() > rvoteParam.getLastLogTerm()){
                            // 没有自己新不给投票
                            log.info("候选人:>{}最后一次的日志任期没有自己大，不给候选人投票。", rvoteParam.getCandidateId());
                            return RvoteResult.fail();
                        }
                        // 判断日志最后的下标
                        if (nodeCore.getLogModule().getLastIndex() > rvoteParam.getLastLogIndex()){
                            // 没有自己新不给投票
                            log.info("候选人:>{}最后一次的日志下标没有自己大，不给候选人投票。", rvoteParam.getCandidateId());
                            return RvoteResult.fail();
                        }
                    }

                    // 准备数据投票给对方切换节点状态(把自己设置为跟随者(FOLLOWER))、
                    // 设置leader节点对象、更改任期、更改选票ID(ip:self)、返回参选成功
                    nodeCore.setState(NodeStatus.FOLLOWER);
                    nodeCore.getPeerSet().setLeader(new Peer(rvoteParam.getCandidateId()));
                    nodeCore.setCurrentTerm(rvoteParam.getTerm());
                    // 候选人的服务id(ip:self)
                    nodeCore.setVotedForId(rvoteParam.getServiceId());
                    log.info("给候选人:>{}投票,把自己设置成跟随者:{}", rvoteParam.getCandidateId(),
                            nodeCore.getPeerSet().getSelf());
                    return rvoteResult.term(nodeCore.getCurrentTerm()).voteGranted(true).build();
                }
            }else{
                log.info("未获取到投票的锁对象.");
            }
            return rvoteResult.term(nodeCore.getCurrentTerm()).voteGranted(false).build();
        }
        finally {
            // 开始解锁
            log.info("投票过程已完成,开始解锁。");
            voteLock.unlock();
        }
    }

    /**
     * 跟随者的日志数据都以leader的为主
     * 接收者实现
     *  一、如果term(投票人的任期) < currentTerm(自身当前的任期)，则返回false；
     *  二、如果日志在prelogindex 位置处的日志条目任期号和 prelogterm 不匹配，则返回false；
     *  三、如果已经存在的日志条目和新的产生冲突（索引值相同但是任期不同），删除这一条和之后所有的；
     *  四、附加任何在已有的日志中不存在的条目
     *     如果AentryParam leadercommit > commitindex,令 commitindex 等于 leadercommit 和 新日志条目中索引值最小的一个;
     * @param aentryParam 附加日志对象
     * @return
     */
    @Override
    public AentryResult requestAentry(AentryParam aentryParam) {
        try {
            AentryResult result = AentryResult.fail();

            if (aentryLock.tryLock()) {
                // TODO 写入日志会是一个瓶颈点
                log.info("开始复制日志,当前节点:>{},当前节点状态:>{},任期:>{}", nodeCore.getPeerSet().getSelf(),
                        NodeStatus.NodeStatusEnum.enumValue(nodeCore.getState()),
                        nodeCore.getCurrentTerm());
                // 给返回值中设置自己的任期
                result.setTerm(nodeCore.getCurrentTerm());
                // 表示候选人的任期没有自己的大,不考虑给他投票
                if (nodeCore.getCurrentTerm() > aentryParam.getTerm()){
                    log.info("候选人:>{}的任期:>{}没有自己的任期:>{}大,不考虑给他投票.",
                            aentryParam.getServiceId(), aentryParam.getTerm(), nodeCore.getCurrentTerm());
                    return result;
                }

                // 设置心跳、选举开始时间(上一次时间)
                nodeCore.setPreHeartBeatTick(System.currentTimeMillis());
                nodeCore.setPreElectionTime(System.currentTimeMillis());
                // todo 心跳过来后，直接刷新当前节点的leader属性，让从节点知道谁是leader
                nodeCore.getPeerSet().setLeader(new Peer(aentryParam.getLeaderId()));

                // 对方的任期比我自身大
                if (aentryParam.getTerm() >= nodeCore.getCurrentTerm()){
                    log.info("当前节点信息:>{}, 当前任期:>{},请求任期:>{}, 请求服务地址:>{}",
                            nodeCore.getPeerSet().getSelf(), nodeCore.getCurrentTerm(), aentryParam.getTerm(), aentryParam.getServiceId());
                    // 设置自己为跟随者
                    nodeCore.setState(NodeStatus.FOLLOWER);
                }
                // 重置自己的任期
                nodeCore.setCurrentTerm(aentryParam.getTerm());

                // 心跳判断
                if (aentryParam.isKeepLive()){
                    log.info("当前节点:>{}接收话事人:>{}心跳请求成功.话事人任期:>{},当前节点任期:>{},心跳状态:>{}",
                            nodeCore.getPeerSet().getSelf(),
                            aentryParam.getLeaderId(),
                            aentryParam.getTerm(),
                            nodeCore.getCurrentTerm(),
                            aentryParam.isKeepLive());
                    // 响应话事人请求，给话事人发送当前节点任期、心跳成功
                    return AentryResult.newBuilder().term(nodeCore.getCurrentTerm()).success(true).build();
                }

                // 日志下标与任期分析
                if (nodeCore.getLogModule().getLastIndex() != 0 && aentryParam.getPrevLogIndex() != 0){
                    // 日志对象
                    LogEntry logEntry;
                    // 请求日志的prevlogindex下标可以查到本节点的日志下标对象
                    if ((logEntry = nodeCore.getLogModule().read(aentryParam.getPrevLogIndex())) != null){
                        // 如果日志可以获取到，开始检查日志任期是否匹配
                        // 复制日志时需要递减下标然后重试
                        if (logEntry.getTerm() != aentryParam.getPrevLogTerm()){
                            log.info("请求附加的日志任期>:{}，与获取到的日志任期>:{}不一致， 需要递减下标值重试>:{}, 服务ID:{}",
                                    aentryParam.getPrevLogTerm(),logEntry.getTerm(), aentryParam.getServiceId());
                            return result;
                        }
                    }else {
                        // 请求的日志下标未能检索到当前节点的日志对象，复制日志时需要递减下标值重试
                        log.info("请求的日志下标未能检索到当前节点的日志对象，需要递减下标值重试>:{}, 服务ID:{}",
                                aentryParam.getPrevLogIndex(), aentryParam.getServiceId());
                        return result;
                    }
                }

                // 如果已经存在的日志条目与新的产生冲突（索引值相同但是任期号不同），删除这一条和之后所有的日志数据
                LogEntry existLog = nodeCore.getLogModule().read(aentryParam.getPrevLogIndex() + 1);
                if (existLog != null && existLog.getTerm() != aentryParam.getLogEntrys()[0].getTerm()){
                    // 删除这一条和之后所有的数据，然后写入日志和状态机
                    nodeCore.getLogModule().removeOnStartIndex(aentryParam.getPrevLogIndex() + 1);
                }else if (existLog != null){
                    // 当前节点已经存在该条日志了不能重读写入
                    result.setSuccess(true);
                    return result;
                }

                // 批量写入附加日志，并且应用至状态机
                for (LogEntry newLogEntry : aentryParam.getLogEntrys()){
                    nodeCore.getLogModule().write(newLogEntry);
                    nodeCore.getStateMachine().apply(newLogEntry);
                    // TODO 这块有问题，只能记录最后一条的日志数据写入状态
                    result.setSuccess(true);
                }

                // 如果leadercommit > commitindex,令 commitindex 等于 leadercommit 和 新日志条目中索引值最小的一个;
                if (aentryParam.getLeaderCommit() > nodeCore.getCommitMaxIndex()){
                    int commitIndex = (int)Math.min(aentryParam.getLeaderCommit(), nodeCore.getLogModule().getLastIndex());
                    nodeCore.setCommitMaxIndex(commitIndex);
                    nodeCore.setLastApplied(commitIndex);
                }

                // 设置节点状态
                result.setTerm(nodeCore.getCurrentTerm());
                nodeCore.setState(NodeStatus.FOLLOWER);
                log.info("提交日志节点操作状态。任期>:{},节点状态>:{}", result.getTerm(), nodeCore.getState());
                return result;
            }else{
                log.info("附加日志操作:<给本地写日志数据>,未获取到锁对象");
            }
            return AentryResult.fail();
        } finally {
            // 开始解锁
            log.info("附加日志处理过程已完成,开始解锁。");
            aentryLock.unlock();
        }
    }
}
