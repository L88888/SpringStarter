package com.tgl.raft.core;

import com.tgl.raft.entity.LogEntry;

/**
 * @program: spring-starter
 * @description: 日志模块
 * @author: LIULEI-TGL
 * @create: 2021-05-20 15:38:
 **/
public interface LogModule {

    /**
     * 写入、读取、删除、获取最新日志对象、获取最新下标
     */

    /**
     * 写入日志条目数据对象
     * @param logEntry
     */
    public void write(LogEntry logEntry);

    /**
     * 读取日志条目数据对象
     * @return
     */
    public LogEntry read(Long index);

    /**
     * 从指定的下标索引位置开始删除日志条目对象
     * @param startIndex 删除的索引下标
     */
    public void removeOnStartIndex(Long startIndex);

    /**
     * 获取最新的日志条目对象
     * @return
     */
    public LogEntry getLast();

    /**
     * 获取最新的日志条目对象下标值
     * @return
     */
    public Long getLastIndex();
}
