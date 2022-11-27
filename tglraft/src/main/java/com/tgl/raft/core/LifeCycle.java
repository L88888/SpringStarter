package com.tgl.raft.core;

/**
 * @program: spring-starter
 * @description: 生命周期接口，用来管理节点、一致性、状态机、心跳、RPC等接口对象
 * @author: LIULEI-TGL
 * @create: 2021-05-20 15:34:
 **/
public interface LifeCycle {

    /**
     * 初始化动作
     * init
     */
    public void init() throws Throwable;

    /**
     * 销毁时停止RPC服务
     * destroy
     */
    public void destroy()throws Throwable;
}
