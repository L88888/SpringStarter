package com.tgl.raft.core;

import com.tgl.raft.entity.AentryParam;
import com.tgl.raft.entity.AentryResult;
import com.tgl.raft.entity.RvoteParam;
import com.tgl.raft.entity.RvoteResult;

/**
 * @program: spring-starter
 * @description: 一致性共识算法raft核心接口>>一致性模块
 * @author: LIULEI-TGL
 * @create: 2021-05-16 18:28:
 **/
public interface Consensus {

    /**
     * 请求投票RPC
     * 接收者实现：
     *   一、如果任期小于当前任期返回false，term < currentTerm return false
     *   二、如果 votedFor （为投票）为空或者就是 candidateId，并且候选人的日志至少和自己的一样新，那么就投票给他
     * @param rvoteParam 候选人投票参数对象，任期与日志索引必须要跟leader保持一致
     * @return
     */
    public RvoteResult requestVote(RvoteParam rvoteParam);


    /**
     * 附加日志(多个日志，为了提高效率) RPC
     * 接收者实现:
     *  一、如果term(投票人的任期) < currentTerm(自身当前的任期)，则返回false；
     *  二、如果日志在prelogindex 位置处的日志条目任期号和 prelogterm 不匹配，则返回false；
     *  三、如果已经存在的日志条目和新的产生冲突（索引值相同但是任期不同），删除这一条和之后所有的；
     *  四、附加任何在已有的日志中不存在的条目
     *     如果leadercommit > commitindex,令 commitindex 等于 leadercommit 和 新日志条目中索引值最小的一个;
     * @param aentryParam 附加日志对象
     * @return
     */
    public AentryResult requestAentry(AentryParam aentryParam);
}
