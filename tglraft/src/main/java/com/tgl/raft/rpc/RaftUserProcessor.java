package com.tgl.raft.rpc;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AbstractUserProcessor;
import com.tgl.raft.exption.RaftNotSupperException;

/**
 * @program: spring-starter
 * @description: 客户端消息处理逻辑
 * @author: LIULEI-TGL
 * @create: 2021-05-22 16:51:
 **/
public abstract class RaftUserProcessor<T> extends AbstractUserProcessor<T> {

    /**
     * 处理异步消息信息 TODO Raft算法不支持异步消息处理
     * @param bizCtx
     * @param asyncCtx
     * @param request
     */
    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, T request) {
        throw new RaftNotSupperException(
                "Raft Server not support handleRequest(BizContext bizCtx,AsyncContext asyncCtx, T request)");
    }

    /**
     * 处理同步消息信息
     * @param bizCtx
     * @param request
     * @return
     * @throws Exception
     */
//    @Override
//    public Object handleRequest(BizContext bizCtx, T request) throws Exception {
//        return null;
//    }

    /**
     * 用户请求的类型，使用字符串避免类加载问题
     * @return
     */
    @Override
    public String interest() {
        return Request.class.getName();
    }
}
