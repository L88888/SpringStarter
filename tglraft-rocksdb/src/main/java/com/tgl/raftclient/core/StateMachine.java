package com.tgl.raftclient.core;

import com.tgl.raftclient.entity.LogEntry;
import com.tgl.raftclient.entity.Command;

/**
 * @program: spring-starter
 * @description: 状态机接口:>> 新增日志，查询，删除日志条目数据
 * @author: LIULEI-TGL
 * @create: 2021-05-16 19:28:
 **/
public interface StateMachine {

    /**
     * 将数据应用到状态机
     * @param logEntry 日志条目对象
     */
    void apply(LogEntry logEntry);

    /**
     * 根据key获取日志条目对象
     * @param key
     * @return
     */
    LogEntry get(String key);

    /**
     * 根据key获取value值对象
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * 设置日志条目对象
     * @param key key值
     * @param value 数据对象
     */
    void setString(String key,String value);

    /**
     * 删除指定key对象信息
     * @param keys
     */
    void delString(String... keys);

    /**
     * 根据key获取业务状态机数据
     * @param key 状态机的key
     * @return 命令行对象字符串数据
     */
    String getCommandData(String key);

    /**
     * 根据key获取业务状态机对象
     * @param key 状态机的key
     * @return 命令行对象数据
     */
    Command getCommandInfo(String key);
}
