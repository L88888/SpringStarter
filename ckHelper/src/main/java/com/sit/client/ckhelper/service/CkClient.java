package com.sit.client.ckhelper.service;

import java.util.List;

/**
 * @program: spring-starter
 * @description: 客户端基础API服务定义
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-16 23:39:
 **/
public interface CkClient {

    /**
     * 查询统计数据
     *
     * @param sql   定义查询的sql文
     * @param clazz 定义查询结果的对象结构
     * @param <T>   待返回的数据结果集泛型
     * @return
     */
    public <T> List<T> queryStaticsData(String sql, Class<T> clazz);

    /**
     * 查询业务数据
     *
     * @param sql   定义查询的sql文,可支持带条件where查询
     * @param clazz 定义查询结果的对象结构
     * @param <T>   待返回的数据结果集泛型
     * @return
     */
    public <T> List<T> queryData(String sql, Class<T> clazz);

    /**
     * 执行一些带计算的业务
     * @param sql 定义查询的sql文,可支持带条件where查询
     * @return 操作状态
     */
    public String execDataOperation(String sql);

    /**
     * 新增业务数据
     * @param sql 定义新增的sql文
     * @param rows 定义业务数据行对象
     * @return
     */
    public String insertRecord(String sql, Object[] rows);

    /**
     * 新增批量业务数据
     * @param sql 定义批量新增的sql文
     * @param rows 定义批量新增的业务数据集合
     * @return
     */
    public String batchInsertRecord(String sql, List<Object[]> rows);

    /**
     * 可通过分区数据值直接删除指定分区的数据（一般较为常用）
     * @param sql 定义分区删除的sql文
     * @return
     */
    public String dropPartitionRecord(String sql);
}
