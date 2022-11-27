# ckHelper

这是一款轻量级click house客户端应用组件。

###### 一、组件支持的功能

1. ​    支持http协议直接对接ck服务端;
2. ​    内嵌单表最基础orm(CRUD)接口封装及实现;
3. ​    支持集群与单机模式下链接ck服务端;
4. ​    支持单表业务单元测试用例示例；
5. ​    支持任意sql单元操作；
6. ​    可对查询结果集数据自动进行泛型封装；
7. ​    无需引入数据源与jdbc驱动组件,例如：(clickhouse-jdbc)；

​       注：可简单解决一个服务中多个数据源来回切换的一些问题。

###### 二、应用场景

   可在任意业务服务当中链接clickhouse，并处理你的数据。

###### 三、API接口

- ​    查询统计数据功能   	
  ​        public <T> List<T> queryStaticsData(String sql, Class<T> clazz);
- ​    查询业务数据功能   	
  ​        public <T> List<T> queryData(String sql, Class<T> clazz);
- ​    执行一些带计算的业务 
     ​	public String execDataOperation(String sql);
- ​    新增业务数据功能    	
  ​       public String insertRecord(String sql, Object[] rows);
- ​    新增批量业务数据功能 
     ​	public String batchInsertRecord(String sql, List<Object[]> rows);
- ​    删除分区数据
  ​    	public String dropPartitionRecord(String sql);

###### 四、测试脚本

	-- 创建一个测试表
CREATE TABLE default.metrics (
    device_id FixedString(36),
    metric String,
    time UInt64,
    value Float64
)ENGINE = MergeTree()
PARTITION BY (time)
ORDER BY (time)
SETTINGS index_granularity = 8192;

-- 创建一个影子库表
CREATE TABLE default.metrics_shadow (
    device_id FixedString(36),
    metric String,
    time UInt64,
    value Float64
)ENGINE = MergeTree()
PARTITION BY (time)
ORDER BY (time)
SETTINGS index_granularity = 8192;

###### 四、后续计划

1.    将组件封装成一个spring-starter模块；
2.    增加ck集群运行状态检测功能；
3.    增加sql语句结构化，降低字符串拼接复杂度；