package com.tgl.raft.impl;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.util.StringUtils;
import com.tgl.client.ClientKVAck;
import com.tgl.client.ClientKVReq;
import com.tgl.raft.cluster.NodeConfig;
import com.tgl.raft.cluster.NodeStatus;
import com.tgl.raft.cluster.Peer;
import com.tgl.raft.cluster.PeerSet;
import com.tgl.raft.core.Consensus;
import com.tgl.raft.core.LogModule;
import com.tgl.raft.core.NodeCore;
import com.tgl.raft.core.StateMachine;
import com.tgl.raft.concurrent.TglRaftThreadPool;
import com.tgl.raft.entity.*;
import com.tgl.raft.membership.changes.ClusterMembershipChanges;
import com.tgl.raft.membership.changes.Result;
import com.tgl.raft.rpc.*;
import com.tgl.raft.util.RaftUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.tgl.raft.rpc.Request.A_ENTRY;

/**
 * @program: spring-starter
 * @description: 默认节点实现，调度一致性接口、日志附加接口、状态机接口
 * @author: LIULEI-TGL
 * @create: 2021-05-20 19:25:
 **/
@Setter
@Getter
@Slf4j
public class DefaultNodeCoreImpl<T> implements NodeCore<T> , ClusterMembershipChanges{

    /** 当前节点状态state ，默认跟随状态 */
    private volatile int state = NodeStatus.FOLLOWER;

    /** 管理核心节点 */
    private PeerSet peerSet;

    /* ===========rpc 以及状态机管理===========start */
    /** 用于客户端发送消息给服务器 */
    private RaftRpcClient raftRpcClient = new DefaultRaftRpcClient();
    /** 用于处理客户端请求消息 */
    private static RaftRpcServer RPC_SERVER;
    /** 状态机对象 */
    private StateMachine stateMachine;
    /** 控制初始化对象的标记，初始化只能有一次 */
    private volatile boolean started = false;
    /** 节点基础配置，自身节点以及集群中所有节点集合 */
    private NodeConfig nodeConfig;
    /* ===========rpc 以及状态机管理===========end */

    /* ==========所有服务器上经常改变的===============start */
    /** 已知的最大的已经被提交的日志条目的最大索引值 */
    private volatile long commitMaxIndex = 0;
    /** 最后被应用到状态机的日志条目索引值（初始化为0，持续递增） */
    private volatile long lastApplied = 0;
    /** 对于每一个服务器，需要发送给它的下一条日志条目的索引值（初始化为领导人最后索引值加一） */
    private Map<Peer, Long> nextIndexs;
    /** 对于每一个服务器，已经复制给它的最高(最大)的索引值,保证每一台机器上的索引值是一样的 */
    private Map<Peer, Long> matchIndexs;
    /* ==============================================end */

    /* ====================一致性&集群模式 ===========start */
    /** 一致性模块实现 */
    private Consensus consensus;

    /** 集群模式变更实现（新增、删除节点） delegate (代表委托)*/
    private ClusterMembershipChanges delegate;
    /* ==============================================end */

    /* =============所有服务器上持久存在的对象&logModule附加日志&currentterm&候选人的投票ID(votedForID)===============start */
    /** 日志条目集合，每一个条目包含一个用户状态机执行的指令，和收到的任期号 */
    private LogModule logModule;
    /** 当前节点服务器最后一次知道的任期号(初始化为0，次序递增) */
    private volatile long currentTerm = 0;
    /** 在当前获得选票的候选人的ID */
    private volatile String votedForId;
    /* ======================================================================================================end*/

    /* ===========选举与心跳对应设置===========start */
    private volatile long electionTime = 0;
    /** 选举时间间隔基数,默认10秒一次 */
    private volatile long elecationInterval = 10 * 1000;
    /** 上一次选举时间记录 */
    private volatile long preElectionTime = 0;
    /** 心跳时间基数,默认2秒一次 */
    private volatile long heartBeatTick = 5 * 1000;
    /** 上一次心跳时间记录 */
    private volatile long preHeartBeatTick = 0;
    /** 日志复制超时时间 */
    private volatile long logCopyTimeOut = 20 * 1000L;
    /* ====================================end */

    /* ============定义心跳、选举、日志复制失败重试队列================= */
    ElectionTask electionTask = new ElectionTask();
    HeartBeatTask heartBeatTask = new HeartBeatTask();
    ReplicationFailQueueConsumer replicationFailQueueConsumer = new ReplicationFailQueueConsumer();
    /** 复制日志失败的阻塞队列,replicationFailModels(LinkedBlockingQueue) */
    LinkedBlockingQueue<ReplicationFailModel> replicationFailModels = new LinkedBlockingQueue<>(2048);

    private DefaultNodeCoreImpl(){}

    public static final DefaultNodeCoreImpl getInstance(){
        return DefaultNodeCoreLazyHolder.getInstance();
    }

    private static final class DefaultNodeCoreLazyHolder{
        private volatile static DefaultNodeCoreImpl INSTANCE;
        public static DefaultNodeCoreImpl getInstance(){
            if (INSTANCE == null){
                synchronized (DefaultNodeCoreLazyHolder.class){
                    if (INSTANCE == null){
                        return INSTANCE = new DefaultNodeCoreImpl();
                    }
                }
            }
            return INSTANCE;
        }
    }


    /**
     * 初始化节点配置
     * @throws Throwable
     */
    @Override
    public void init() throws Throwable {
        if (started){
            log.info("初始化节点配置已经完成请勿重复初始化.");
            return;
        }

        // 只能初始化一次
        synchronized(this){
            // 两次检查防止重复创建对象
            if (started){
                return;
            }

            if (Objects.isNull(RPC_SERVER)){
                log.warn("RCP_SERVER 服务没有初始化,程序退出.");
                System.exit(0);
            }

            // 启动rpc服务端
            RPC_SERVER.start();
            // 初始化一致性接口,this表示当前对象
            consensus = new DefaultConsensusImpl(this);
            // 初始化集群接口，this表示当前对象
            delegate = new ClusterMembershipChangesImpl(this);

            // 启动定时任务心跳、选举、失败队列重试机制
            // 不带延迟开启心跳,per半秒(500ms)一个定时任务
            TglRaftThreadPool.scheduleWithFixedDelay(heartBeatTask, 1000, TimeUnit.MILLISECONDS);
            // 延迟启动6秒后开始选举产生一个话事人,延迟6秒开始执行调度任务,per半秒(500ms)一个定时任务
            TglRaftThreadPool.scheduleAtFixdRate(electionTask, 6 * 1000,1000, TimeUnit.MILLISECONDS);
            TglRaftThreadPool.execute(replicationFailQueueConsumer);

            // 获取最后一条日志对象的日志任期
            LogEntry logEntry = logModule.getLast();
            if (logEntry != null){
                setCurrentTerm(logEntry.getTerm());
            }

            started = true;
            log.info("初始化节点成功，自身id为:{}", getPeerSet().getSelf());
        }
    }

    /**
     * 初始化集群配置
     * @param nodeConfig
     */
    @Override
    public void setConfig(NodeConfig nodeConfig) {
        setNodeConfig(nodeConfig);
        // todo 需要放在init()?
        logModule = DefaultLogEntryImpl.getInstance();
        stateMachine = DefaultStateMachineImpl.getInstance();

        // 初始化集群节点
        peerSet = PeerSet.getInstance();
        // 遍历节点集合
        for (String perAddr : nodeConfig.getPerAddrs()){
            if (StringUtils.isBlank(perAddr)){
                continue;
            }

            // 添加集群节点包括自身self
            Peer peer = new Peer(perAddr);
            peerSet.addPeer(peer);
            // 添加自身节点,分布式开发必须考虑当前节点与集群其他节点
            if (Objects.equals(perAddr, "localhost:" + nodeConfig.getSelfPort())){
                peerSet.setSelf(peer);
            }
        }

        log.info("初始化集群节点信息:>{}", peerSet);
        // 初始化rpcserver端对象
        RPC_SERVER = new DefaultRaftRpcServer(nodeConfig.getSelfPort(), this);
    }

    /**
     * 处理投票请求
     * @param rvoteParam 投票对象
     * @return
     */
    @Override
    public RvoteResult handlerRequestRvote(RvoteParam rvoteParam) {
        log.info("接收到的请求投票对象为:>{}", rvoteParam);
        return consensus.requestVote(rvoteParam);
    }

    /**
     * 处理附加日志请求
     * @param aentryParam 附加日志数据对象
     * @return
     */
    @Override
    public AentryResult handlerRequestAentry(AentryParam aentryParam) {
        if (aentryParam.getLogEntrys() != null){
            log.warn("节点:>{},接收并追加日志数据:>{}", aentryParam.getLeaderId(), aentryParam.getLogEntrys());
        }
        return consensus.requestAentry(aentryParam);
    }

    /**
     * 处理客户端请求
     * 1、客户端的每一个请求都包含一条被复制状态机的执行命令。
     * 2、话事人把这条指令作为一条新的日志附加到日志条目中去，然后通过并行RPC的方式复制到其他的节点（服务器），让他们复制这条日志条目。
     * 3、当这条日志条目被完全的复制（所有节点都已经复制完成），话事人会应用这条日志条目到他的状态机中，然后把执行结果返回给客户端。
     * 4、如果其他节点出现网络异常、丢包、运行缓慢，话事人会尝试不断的重试RPC（尽管已经回复了客户端）直到所有的节点都最终存储了所有的日志条目。
     * synchronized 保证节点日志与状态机数据一致性
     * @param clientKVReq 客户端请求对象
     * @return
     */
    @Override
    public synchronized ClientKVAck handlerClientRequest(ClientKVReq clientKVReq) {
        log.warn("处理客户端请求逻辑.请求类型type:>{},key:>{},value:>{}",
                ClientKVReq.ReqType.enumValue(clientKVReq.getType()),clientKVReq.getKey(), clientKVReq.getValue());
        // 主要处理各节点日志同步与复制机制
        if (getState() != NodeStatus.LEADER){
            // 表示需要进行请求转发
            log.info("当前节点并非话事人:>{}节点,当前节点信息为:>{}", getPeerSet().getLeader(), getPeerSet().getSelf().getAddr());
            return redirect(clientKVReq);
        }

        // 判断请求类型;get获取状态机key对应的业务数据、post类型为新增客户端日志数据;
        // todo 客户端请求并发过多时会出现话事人节点响应过慢的问题，需要考虑其他节点也参与客户端请求数据(类似于做请求代理转发)
        // 状态机的负责获取是一个问题，这里只是在leader节点进行数据获取
        if (clientKVReq.getType() == ClientKVReq.GET){
            // 获取状态机日志
            LogEntry logEntry = stateMachine.get(clientKVReq.getKey());
            if (logEntry != null){
                return new ClientKVAck(logEntry);
            }
            return new ClientKVAck(null);
        }

        // 封装日志对象logentry
        LogEntry logEntryData = LogEntry.newBuilder()
                .command(Command.newBuilder()
                        .key(clientKVReq.getKey())
                        .value(clientKVReq.getValue())
                        .build())
                .term(getCurrentTerm())
                .build();
        // 本地预提交日志下标
        logModule.write(logEntryData);
        log.info("leader节点:>{}写入日志数据成功，日志对象为:>{},日志下标为:>{}", getPeerSet().getLeader(),
                logEntryData, logEntryData.getIndex());

        // 开始给集群中的其他节点复制请求日志对象
        // todo 并发进行日志数据拷贝复制
        final AtomicInteger success = new AtomicInteger(0);
        // CopyOnWriteArrayList 读多写少
        List<Future<Boolean>> resList = new CopyOnWriteArrayList<>();

        // 获取机器节点数量
        int countNode = 0;
        for (Peer p : peerSet.getPeersWithOutSelf()){
            countNode++;
            resList.add(replication(p, logEntryData));
        }

        // 异步收集节点日志复制的结果，用来控制多线程并发时的数据安全保护
        CountDownLatch countDownLatch = new CountDownLatch(resList.size());
        // 安全的写入(读多写少)
        List<Boolean> processList = new CopyOnWriteArrayList<>();
        // 统一使用引用传递的方式来处理业务
        getRPCAppendResult(resList, countDownLatch, processList);

        try {
            // 最大等待4秒时间，回收日志复制的结果集对象
            countDownLatch.await(4000, TimeUnit.MILLISECONDS);
            log.info("各节点日志复制总长度为:>{},结果为:>{}", processList.size(), processList.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 过滤日志复制成功的个数，使用原子变量AtomicInteger对象存储
        for (Boolean resData : processList){
            if (resData){
                success.incrementAndGet();
            }
        }

        // 如果存在一个满足M > commitIndex的 M，并且大多数的matchIndex[i] ≥ M成立，
        // 并且log[M].term == currentTerm成立，那么令 commitIndex 等于这个 M （5.3 和 5.4 节）
        // 计算commitIndex
        List<Long> matchIndexList = new ArrayList<>(getMatchIndexs().values());
        int median = 0;
        if (matchIndexList.size() >= 2){
            // 默认升序排列
            Collections.sort(matchIndexList);
            median = matchIndexList.size() / 2;
        }

        Long M = matchIndexList.get(median);
        if (M > getCommitMaxIndex()){
            LogEntry log = logModule.read(M);
            if (log != null && log.getTerm() == getCurrentTerm()){
                setCommitMaxIndex(M);
            }
        }

        // 成功一半或者以上(countNode / 2)获取一半的节点数量
        if (success.get() >= (countNode / 2)){
            // 更新提交下标commitIndex
            setCommitMaxIndex(logEntryData.getIndex());
            // 应用状态机，应用到自己的节点上
            stateMachine.apply(logEntryData);
            setLastApplied(getCommitMaxIndex());
            log.info("成功复制客户端请求日志至状态机，请求的日志对象为:>{}", logEntryData);
            return ClientKVAck.ok();
        }else {
            // 多数节点日志复制失败
            // 回滚本地预提交的日志对象
            logModule.removeOnStartIndex(logEntryData.getIndex());
            log.warn("没有成功复制过半的节点，不给状态机中添加日志数据, 请求的日志数据对象为:>{}", logEntryData);
            // TODO 不应用到状态机中，但是已经记录到日志中，需要由定时任务从重试队列中取出请求的日志数据，然后重复尝试，当达到条件时应用到状态机
            return ClientKVAck.fail();
        }
    }

    /**
     * 由leader统一进行请求转发，通过客户端给服务器端发送请求消息
     * // todo需要考虑在选举的过程中如果有消息发过来，这时的leader还么有产生怎么办？
     * @param clientKVReq 客户端请求对象
     * @return
     */
    @Override
    public ClientKVAck redirect(ClientKVReq clientKVReq) {
        try {
            // todo 如果当前节点不是leader节点，那么将接收到的消息直接转发给leader节点
            Request request = Request.newBuilder()
                    .cmd(Request.CLIENT_REQ)
                    .url(peerSet.getLeader().getAddr())
                    .obj(clientKVReq)
                    .build();
            // 由服务器端的handleClientRequest接口统一处理请求
            Response response = getRaftRpcClient().send(request);
            return (ClientKVAck) response.getResult();
        } catch (RemotingException e) {
            log.warn("由leader转发客户端请求异常,异常信息为:>{}", e.fillInStackTrace());
            return null;
        }
    }

    @Override
    public void destroy() throws Throwable {
        RPC_SERVER.stop();
    }

    @Override
    public Result addPeer(Peer newPeer) {
        return delegate.addPeer(newPeer);
    }

    @Override
    public Result removePeer(Peer oldPeer) {
        return delegate.removePeer(oldPeer);
    }

    /**
     * 统一收集节点日志复制结果，开并行处理每次只能有一个线程执行
     * @param futureList 执行返回值对象,引用传递
     * @param countDownLatch 控制线程执行频率,引用传递
     * @param resultList 回收返回结果集,引用传递
     */
    private void getRPCAppendResult(List<Future<Boolean>> futureList,
                                    CountDownLatch countDownLatch,
                                    List<Boolean> resultList){
        if (futureList == null || futureList.size() == 0){
            return;
        }

        log.warn("开始收集各个节点日志复制的结果对象.");
        // 开并行处理结果集
        for (Future<Boolean> future : futureList){
            TglRaftThreadPool.execute(()->{
                try {
                    // per个节点日志复制的结果，最大延迟3秒来获取得到结果值
                    resultList.add(future.get(3000, TimeUnit.MILLISECONDS));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } finally {
                    // 线程执行对象递减
                    countDownLatch.countDown();
                }
            });
        }
    }

    /**
     * 开启并行复制日志给指定的节点
     * @param peer 新节点对象
     * @param logEntry 待复制的日志对象
     * @return
     */
    public Future replication(Peer peer, LogEntry logEntry){
        return TglRaftThreadPool.submit(new Callable() {
            @Override
            public Boolean call() throws Exception {
                log.info("开始给节点:>{}复制日志.", peer);
                long startTime = System.currentTimeMillis();
                long endTime = startTime;
                // 20秒重试时间,20秒没有复制完日志直接超时
                while (endTime - startTime < getLogCopyTimeOut()){
                    try {
                        log.info("当前节点任期:>{}, 日志提交的最大索引值:>{}, 当前节点信息(IP:port):>{}", getCurrentTerm()
                                , getCommitMaxIndex()
                                , getPeerSet().getSelf().getAddr());
                        // 组装日志请求对象AentryParam
                        AentryParam aentryParam = new AentryParam();
                        aentryParam.setTerm(getCurrentTerm());
                        aentryParam.setServiceId(peer.getAddr());
                        aentryParam.setLeaderId(getPeerSet().getSelf().getAddr());
                        aentryParam.setLeaderCommit(getCommitMaxIndex());

                        // 获取下一条日志的索引值
                        Long nextIndex = nextIndexs.get(peer);
                        // LinkedList按照顺序存储日志对象LogEntry
                        LinkedList<LogEntry> logEntries = new LinkedList<>();
                        // 请求的日志数据参数，大于下一条日志对象时，取出请求日志对象中所有的日志条目
                        if (logEntry.getIndex() >= nextIndex){
                            LogEntry tempLogEntry = null;
                            for (long i = nextIndex;i <= logEntry.getIndex();i++){
                                tempLogEntry = logModule.read(i);
                                if (tempLogEntry != null){
                                    logEntries.add(tempLogEntry);
                                }
                            }
                        }else {
                            // 小于的情况下直接添加附加日志对象
                            logEntries.add(logEntry);
                        }

                        // 取第一个日志对象，的前一个任期数据getPreLog()
                        LogEntry preLog = getPreLog(logEntries.getFirst());
                        aentryParam.setPrevLogTerm(preLog.getTerm());
                        aentryParam.setPrevLogIndex(preLog.getIndex());
                        // toArray(new LogEntry[0])指定数组对象类型
                        aentryParam.setLogEntrys(logEntries.toArray(new LogEntry[0]));

                        // 开始封装日志数据对象，进行集群发送待复制的日志对象
                        Request request = Request.newBuilder()
                                .obj(aentryParam)
                                .cmd(A_ENTRY)
                                .url(peer.getAddr())
                                .build();

                        Response response = getRaftRpcClient().send(request);
                        log.info("服务器端响应的结果对象为:>{}", response);
                        if (response == null){
                            log.info("日志复制，服务器端处理失败:>{}", response);
                            return false;
                        }
                        AentryResult aentryResult = (AentryResult) response.getResult();
                        // 每次复制成功后都要更新当前节点最大的日志下标值
                        if (aentryResult != null && aentryResult.isSuccess()){
                            log.warn("追加从节点:>{}日志成功,日志信息为:>{}", peer, aentryParam.getLogEntrys());
                            // 更新节点的最大下标，以及下一个下标值 加一
                            nextIndexs.put(peer, logEntry.getIndex() + 1);
                            matchIndexs.put(peer, logEntry.getIndex());
                            return true;
                        }else if (aentryResult != null){
                            if (aentryResult.getTerm() > getCurrentTerm()){
                                log.info("跟随者:>{}任期为:>{}, 当前节点的任期:>{},比我多所有我的状态成为跟随者.",
                                        peer, aentryResult.getTerm(), getCurrentTerm());
                                // 更改当前节点任期,使用前期返回的任期来更改
//                                currentTerm = aentryResult.getTerm();
                                setCurrentTerm(aentryResult.getTerm());
                                // 更改节点状态为跟随者
//                                state = NodeStatus.FOLLOWER;
                                setState(NodeStatus.FOLLOWER);
                                return false;
                            }else {
                                // 对方任期没有当前节点大，说明index or term 不对
                                // 开始递减
                                if (nextIndex == 0){
                                    nextIndex = 1L;
                                }
                                nextIndexs.put(peer, nextIndex -1);
                                log.info("跟随者:>{},的下一个日志对象下标匹配失败，递减后重试RPC发送过程，下标为:>{}",
                                        peer, nextIndex);
                            }
                        }
                        endTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        // 复制日志失败需要进行入队列重新处理
                        log.warn("复制日志失败需要进行入队列重新处理:>{}", e.fillInStackTrace());
                        ReplicationFailModel replicationFailModel = ReplicationFailModel.newBuilder()
                                .callable(this)
                                .logEntry(logEntry)
                                .offerTime(System.currentTimeMillis())
                                .peer(peer)
                                .build();
                        replicationFailModels.offer(replicationFailModel);
                    }
                }
                // 复制超时
                return false;
            }
        });
    }

    /**
     * 获取前一个下标索引的日志对象
     * @param logEntry
     * @return
     */
    private LogEntry getPreLog(LogEntry logEntry){
        LogEntry resLogEntry = logModule.read(logEntry.getIndex() - 1);
        if (resLogEntry == null ||
                (resLogEntry.getCommand() == null && resLogEntry.getTerm() == 0 && resLogEntry.getIndex() == null)){
            log.info("通过当前日志对象，检索不到pre前一个日志对象数据,当前日志对象为:>{}", logEntry);
            resLogEntry = LogEntry.newBuilder().term(0).index(0L).command(null).build();
        }
        return resLogEntry;
    }

    /**
     * 初始化所有节点的nextIndex值为自己的最后一条日志索引值 (selfIndex + 1), 日过下次RPC时跟随者和leader不一致，就会失败
     * 那么leader将会递减nextindex 并进行重试，直到最终一致为止（最终将会达到一致）
     */
    private void becomeLeaderTodoNode(){
        // 线程安全
        nextIndexs = new ConcurrentHashMap<>();
        matchIndexs = new ConcurrentHashMap<>();
        // 需要做容错校验
        for (Peer peer : peerSet.getPeersWithOutSelf()){
            // 下一次RPC从这个节点往后继续获取
            nextIndexs.put(peer, logModule.getLastIndex() + 1);
            matchIndexs.put(peer, 0L);
        }
    }

    /**
     * 心跳任务对象
     * leader不进行心跳检测
     * 心跳只关心term 和 leaderId
     * todo 心跳只由leader给其他的从节点发送。需要存储未回消息的从节点信息；
     */
    private class HeartBeatTask implements Runnable{

        @Override
        public void run() {
            // 只有leader才具有发送心跳的机制
            if (state != NodeStatus.LEADER){
                return;
            }

            long currentTime = System.currentTimeMillis();
            // 5秒一次心跳,当前时间减去上一次心跳时间 < 心跳间隔时间(5 * 1000L),默认都是大于的
            if (currentTime - getPreHeartBeatTick() < heartBeatTick){
                return;
            }

            log.warn("开始给所有跟随者节点发送心跳.");
            for (Peer peer : peerSet.getPeersWithOutSelf()){
                log.info("跟随者对象:>{}, 跟随者下标nextndex:>{}", peer, nextIndexs.get(peer));
            }
            // 设置本次的心跳执行时间
            setPreHeartBeatTick(System.currentTimeMillis());

            // 心跳只关心任期和leaderId
            for (Peer peer : peerSet.getPeersWithOutSelf()){
                // 封装心跳请求对象AentryParam
                AentryParam aentryParam = AentryParam.newBuilder()
                        .term(currentTerm)
                        // 自己的节点信息
                        .leaderId(peerSet.getSelf().getAddr())
                        // 其他节点信息
                        .serviceId(peer.getAddr())
                        .keepLive(true)
                        .build();

                // 封装请求对象
                Request requestData = Request.newBuilder()
                        .url(peer.getAddr())
                        .cmd(A_ENTRY)
                        .obj(aentryParam)
                        .build();

                // 开启多线程并行执行发送心跳数据，确保跟随者节点都正常可用
                TglRaftThreadPool.execute(()->{
                    try {
                        Response response = getRaftRpcClient().send(requestData);
                        // 判断任期，修改当前节点状态
                        AentryResult aentryResult = (AentryResult) response.getResult();

                        // 对方的任期比自己的大，把自己自动降级为跟随者.把自己的任期设置为对方的任期,投票ID设置为""
                        if (aentryResult.getTerm() > currentTerm){
                            log.debug("当前任期:>{}, 对方返回的任期:>{}", getCurrentTerm(), aentryResult.getTerm());
                            setCurrentTerm(aentryResult.getTerm());
                            setState(NodeStatus.FOLLOWER);
                            setVotedForId("");
                        }
                    } catch (Exception e) {
                        log.warn("给当前节点:>{},发送心跳失败.:>{}", peer, e.fillInStackTrace().toString());
                        // todo失败了需要考虑放到队列中重新发送
                        // 节点心跳发送失败后，需要记录该节点的基本信息，并从集群中删除该节点信息
                        log.info("当前节点:>{},发送心跳失败.将会被移除集群节点", peer);
//                        peerSet.removePeer(peer);
                        // 同时发消息给集群中其他的节点，让他们也一并删除掉失败的节点
                    }
                });
            }
        }
    }

    /**
     * 候选人开始选举leader
     * 1、在转变为候选人之后就立即开始选举过程
     *    1.1、自增自己的任期term
     *    1.2、给自己投票
     *    1.3、重置选举超时计数器（Thread线程随机数）
     *    1.4、发送请求投票的RPC请求消息给其他所有的服务器
     *  2、如果接收到大多数服务器的选票，那么自身的状态就become（成为）话事人
     *  3、如果接收到来自新的领导人的附加日志RPC,那么自己的状态就变为跟随者
     *  4、如果选举过程超时，在发起一次选举
     */
    private class ElectionTask implements Runnable{

        @Override
        public void run() {
            try {

                if (getState() == NodeStatus.LEADER){
                    return;
                }

                long startTime = System.currentTimeMillis();
                // 基于RAFT的随机时间来解决选举冲突问题（活锁）.50毫秒的随机数
                // todo 随着节点运行时间的增加，选举间隔时间会一直增加。这样会导致当leader节点挂掉之后，新的一轮leader选举会推迟几分钟时间。
                // todo 综合考虑需要加快心跳频率，目前是5秒一次，设置为2秒。同时去掉选举时间间隔累加随机数的逻辑。从而提升leader选举速度
                elecationInterval = elecationInterval + ThreadLocalRandom.current().nextInt(50);
                if (startTime - preElectionTime < elecationInterval){
                    // 选举已经结束了,初始化选举间隔时间
    //                log.warn("选举已经结束了.{},{}", preElectionTime, getPreElectionTime());
//                    if (elecationInterval > 15 * 1000l){
//                        elecationInterval = 10 * 1000l;
//                    }
                    return;
                }

                log.info("开始进行leader选举,当前节点:>{},状态为:>{},任期为:>{},选举间隔时间:>{},上一次选举时间:>{},当前时间:>{}", getPeerSet().getSelf(),
                        NodeStatus.NodeStatusEnum.enumValue(getState()),
                        getCurrentTerm(),
                        getElectionTime(),
                        getPreElectionTime(),
                        System.currentTimeMillis());
                // 当选为话事人后需要及时向集群中的节点发送心跳消息，予以告知自己的身份
                log.info("当前集群中话事人为:>{}", Objects.isNull(peerSet.getLeader()) ? "话事人(leader)节点还未诞生." : peerSet.getLeader().getAddr());

                // 把自己设置为候选人状态
                setState(NodeStatus.CANDIDDATE);
                // 200毫秒+,作为一个选举周期
                preElectionTime = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(200) + 150;

                currentTerm++;
                // 设置自己的选票
                setVotedForId(peerSet.getSelf().getAddr());
                // 获取集群中其他的节点
                List<Peer> peers = peerSet.getPeersWithOutSelf();
                log.info("选举前,集群节点信息:>{}.当前节点任期:>{}", peers, getCurrentTerm());
                // 收集每一个节点的选票结果 todo 是否需要使用写多读少的容器Copy?CopyWriteArrayList容器存储数据类型有限制
                ArrayList<Future> futureArrayList = new ArrayList<>();
                for (Peer peer : peers){
                    // 开并行处理各个节点选票的过程与接收选票的结果
                    futureArrayList.add(
                            // 自定义线程池,发送投票信息给其他的节点。生产投票结果至futureArrayList链表;
                            TglRaftThreadPool.submit(new Callable() {
                                @Override
                                public Object call() throws Exception {
                                    // 计算最后的日志任期
                                    long lastTerm = 0L;
                                    LogEntry lastLog = logModule.getLast();
                                    if (lastLog != null){
                                        lastTerm = lastLog.getTerm();
                                    }
                                    log.info("当前节点:>{},最后的日志任期为:>{}", peer.getAddr(), lastTerm);

                                    // 组装选票对象
                                    RvoteParam rvoteParam = RvoteParam.newBuilder()
                                            // 发送自己的节点ip信息，给其他的节点进行选举
                                            .candidateId(peerSet.getSelf().getAddr())
                                            // 获取当前节点最后的日志下标索引值
                                            .lastLogIndex(RaftUtils.getInstance().convert(logModule.getLastIndex()))
                                            .term(currentTerm)
                                            .lastLogTerm(lastTerm)
                                            .build();
                                    // 组装请求对象
                                    Request request = Request.newBuilder()
                                            .obj(rvoteParam)
                                            .url(peer.getAddr())
                                            .cmd(Request.R_VOTE)
                                            .build();

                                    try {
                                        // 发送RPC请求,减少内存对象创建
                                        return (Response<RvoteResult>)getRaftRpcClient().send(request);
                                    } catch (Exception e) {
                                        log.warn("选举消息发送异常:>{},节点:>{}有可能出现网络中断现象.",
                                                e.getMessage(),
                                                peer.getAddr());
                                        // 得到真实的集群节点数量
//                                        peerSet.removePeer(peer);
                                        return null;
                                    }
                                }
                            })
                    );
                }


                // 对选举结果进行逻辑处理
                // atomicInteger 解决线程计数原子性操作
                final AtomicInteger success = new AtomicInteger(0);
                // 记录失败节点数量
                final AtomicInteger fail = new AtomicInteger(0);
                // 解决并发情况下多线程并行问题，一次只能一个线程在执行,线程执行数量递减
                CountDownLatch downLatch = new CountDownLatch(futureArrayList.size());
                for (Future future : futureArrayList){
                    // 消费futureArrayList链表中的投票结果对象
                    TglRaftThreadPool.submit(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            try {
                                // 开始处理选票结果
                                log.info("开始处理选票结果");
                                // todo 是否需要延迟3秒获取投票结果对象？由于是两个线程交替执行，所以需要延迟消费投票结果链表对象。
                                // 不然会出现消费不到投票结果的现象
                                Response<RvoteResult> response0 =
                                        (Response<RvoteResult>) future.get(3000, TimeUnit.MILLISECONDS);
                                if (response0 == null ){
                                    fail.getAndIncrement();
                                    return -1;
                                }

                                // 处理正常的业务
                                // 赞成的票数加一
                                if (response0.getResult().isVoteGranted()){
                                    success.getAndIncrement();
                                }else {
                                    // 自己的任期小于等于对方节点的任期时,更新自己的任期
                                    if (getCurrentTerm() <= response0.getResult().getTerm()){
                                        setCurrentTerm(response0.getResult().getTerm());
                                    }
                                }
                                return 0;
                            } catch (Exception e) {
                                log.warn("选举接收消息异常:>{},有可能出现网络中断现象.", e.getMessage());
                                fail.getAndIncrement();
                                return -1;
                            } finally {
                                // 递减
                                downLatch.countDown();
                            }
                        }
                    });
                }

                try {
                    // 最大等待时间为>3500秒,用来获取所有的节点投票结果，给后期判断是否选中话事人做铺垫
                    downLatch.await(3500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 获取投票个数（赞成的票数）
                int successVortNum = success.get();
                // 节点异常（发送消息没有响应超时）
                int failVortNum = fail.get();
                log.info("当前节点:>{},赞同的节点数量:>{},当前节点的状态:>{},节点异常数量:>{}", peerSet.getSelf(),
                        successVortNum, NodeStatus.NodeStatusEnum.enumValue(getState()), failVortNum);
                // 在选举过程中有其他节点发送appendentry, 当前节点就可能变成follower，选举过程停止
                if (getState() == NodeStatus.FOLLOWER){
                    log.info("选举过程中遇到其他节点的状态为leader,自动结束选举过程:>{}", getState());
                    return;
                }

                // 获得投票过半的节点个数,>=(加上自身的这一票)
                // todo peers.size() / 2 在节点数量少时无法计算出leader节点
                if (successVortNum >= (peers.size() - failVortNum) / 2){
                    log.info("当前节点:>{},becom成为话事人(leader).", peerSet.getSelf());
                    setState(NodeStatus.LEADER);
                    setVotedForId("");
                    // todo 选举成功后将leader设置为自身,特别重要给后期客户端连接提供请求转发消息的帮助与作用
                    peerSet.setLeader(peerSet.getSelf());
                    // 初始化成为leader配置
                    becomeLeaderTodoNode();
                }else {
                    // 话事人选举重新再来一遍
                    setVotedForId("");
                }
            } catch (Exception e) {
                log.warn("leader 节点选举出现异常，异常消息为:>{}", e.fillInStackTrace());
            }
        }
    }

    /**
     * 消费复制失败的队列日志消息
     */
    private class ReplicationFailQueueConsumer implements Runnable{

        @Override
        public void run() {

        }
    }
}
