package com.sailing.rpc.server;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AbstractUserProcessor;
import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @program: spring-starter
 * @description: 客户端消息处理逻辑
 * @author: LIULEI-TGL
 * @create: 2021-05-22 16:51:
 **/
@Slf4j
@Service
public class BusinessProcessor<T> extends AbstractUserProcessor<T> {

    /**
     * 处理异步消息信息 TODO Raft算法不支持异步消息处理
     * @param bizCtx
     * @param asyncCtx
     * @param request
     */
    @Override
    public void handleRequest(BizContext bizCtx, AsyncContext asyncCtx, T request) {
        log.info("该处理器只处理同步消息,不处理异步消息。");
        return;
    }

    /**
     * 处理同步消息信息
     * @param bizCtx
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public Object handleRequest(BizContext bizCtx, T request) throws Exception {
        return proHandlerRequest((Request) request);
    }

    /**
     * 用户请求的类型，使用字符串避免类加载问题
     * @return
     */
    @Override
    public String interest() {
        return Request.class.getName();
    }

    public Response proHandlerRequest(Request request) {
        log.info("同步RPC客户端请求报文:>{},请求对象长度{}"
                , request.getUrl()
                , request.getObj() instanceof ArrayList ? ((ArrayList)request.getObj()).size() : request.getObj());

        // 制造服务器端假死
//        InitRpcServer.closeService();

        if (request == null){
            log.info("请求对象为null，服务器端无法处理.");
            return Response.fail("请求对象为null，服务器端无法处理.");
        }

        // 统一返回处理结果对象
        Response response = null;
        // 判断cmd处理类型
        switch(request.getCmd()){
            case Request.R_VOTE:
                // 处理投票请求
                response = new Response(request.getObj());
                break;
        }
        return response;
    }
}
