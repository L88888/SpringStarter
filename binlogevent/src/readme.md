Java实现目标RAFT需求
1、多节点选取Leader；
2、Leader节点下线重选；
3、Leader发送心跳，根据响应维护Follower存活列表；
4、Node之间数据同步；
5、简单数据事务commit rollback；

raft一致性共识算法，简单理解
1、leader选举；
2、日志复制；
3、成员变更；
4、日志压缩；

raft开始模块开发
一致性模块
RPC通信模块
日志模块
状态机
