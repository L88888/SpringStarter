Java实现目标RAFT需求
1、多节点选取Leader；
2、Leader节点下线重选；
3、Leader发送心跳，根据响应维护Follower存活列表；
4、Node之间数据同步；
5、简单数据事务commit rollback；

raft一致性共识算法，简单理解
1、leader选举；(在leader选举有两种有两个超时设置来控制选举，一、选举超时；二、分裂选举（当两个候选人的选票一样时，开始选举等待重试，谁先结束谁就是领导）)
2、日志复制（领导人给其他的节点发送日志消息，通过心跳间隔来发送日志条目追加）；
3、成员变更；
4、日志压缩；

raft开始模块开发
一、一致性模块：一致性模块是raft算法的核心，通过一致性模块保证raft集群数据的一致性。（这里我们需要根据论文的描述来实现）
二、RPC通信模块：可以使用http短链接，也可以直接使用tcp长连接来实现。考虑到集群各个节点频繁通信，再加上集群各个节点都在一个局域网内。所以选择使用tcp长连接的模式
来进行实现RPC通信。而Java社区长连接框架首选（Netty），这里我们使用蚂蚁金服的网络通信框架SOFA-Bolt（基于Netty），便于快速开发。
三、日志模块：raft算法中日志模块是基础，考虑到时间因素，我们选用rocksdb作为日志存储组件。
四、状态机：可以是任何实现，其本质就是将日志中的内容进行处理。可以理解为mysql binglog中的具体数据。由于我们本次需要实现一个K/V的数据存储，那么可以直接使用日志
模块中的Rocksdb组件。

接口设计
上面我们说的raft四个核心功能，实际上就可以理解为接口。以下是对具体接口的定义
1、Consensus 一致性共识模块接口
2、LogModule 日志模块(用于对各个节点的下标一致性做校验，存储的key统一都是下标索引)
3、StateMachine 状态机接口(用于可对外检索以及存储的key/value业务数据对象,最终还是日志对象logEntry)
4、RPCServer & RPCClient RPC通信协议接口
5、Node 节点，为了聚合上面的几个接口，我们需要定义一个Node接口，即节点的意思，Raft抽象的机器节点
6、LifeCycle 生命周期接口，需要管理以上组件的生命周期，因此需要一个LifeCaycle接口


关键词：term（任期），split（分割），vote（选票），elections（选举），normal （正常），operation（操作），
crash（崩溃）,heartBeat（心跳）,entries（条目）,structure（结构），safety（安全），aentry（附加操作），
majority（多数，大多数）,become(成为),todo(全部),persistent(持久的、耐久的)

选举过程关键词
关键词：start（开始），follower（跟随者），candidate（候选人），leader（领导人）；
discover current leader or higher term（发现当前领导或者更高任期）；
timeout start election（超时开始选举）；
timeout new election (新的选举超时);
receive votes from majority of servers（接收大多数服务器的投票）；
discover server with higher term （发现具有更高任期的服务器）；
step down（下台）；

leader选举验证
//"localhost:13141","localhost:13142","localhost:13143","localhost:13144","localhost:13145"
1. 在 idea 中配置 5 个 application 启动项,配置 main 类为 RaftNodeBootStrap 类,
加入 -DserverPort=13141 -DserverPort=13142 -DserverPort=13143 -DserverPort=13144 -DserverPort=13145
系统配置, 表示分布式环境下的 5 个机器节点.
2. 依次启动 5 个 RaftNodeBootStrap 节点, 端口分别是 13141, 13142, 13143, 13144, 13145.
3. 观察控制台, 约 6 秒后, 会发生选举事件,此时,会产生一个 leader. 而  leader 会立刻发送心跳维持自己的地位.
4. 如果leader 的端口是  13141, 使用 idea 关闭 13141 端口，模拟节点挂掉, 大约 15 秒后, 会重新开始选举, 并且会在剩余的 4 个节点中,
产生一个新的 leader.  并开始发送心跳日志。

日志复制验证
一、正常状态下
1. 在 idea 中配置 5 个 application 启动项,配置 main 类为 RaftNodeBootStrap 类,
加入 -DserverPort=13141 -DserverPort=13142 -DserverPort=13143 -DserverPort=13144 -DserverPort=13145
2. 依次启动 5 个 RaftNodeBootStrap 节点, 端口分别是 13141, 13142, 13143, 13144, 13145.
3. 使用客户端写入 kv 数据.
4. 杀掉所有节点, 使用 junit test 读取每个 rocksDB 的值, 验证每个节点的数据是否一致.

二、非正常状态下
1. 在 idea 中配置 5 个 application 启动项,配置 main 类为 RaftNodeBootStrap 类,
加入 -DserverPort=13141 -DserverPort=13142 -DserverPort=13143 -DserverPort=13144 -DserverPort=13145
2. 依次启动 5 个 RaftNodeBootStrap 节点, 端口分别是 13141, 13142, 13143, 13144, 13145.
3. 使用客户端写入 kv 数据.
4. 杀掉 leader （假设是 13141）.
5. 再次写入数据.
6. 重启 13141.
7. 关闭所有节点, 读取 RocksDB 验证数据一致性.