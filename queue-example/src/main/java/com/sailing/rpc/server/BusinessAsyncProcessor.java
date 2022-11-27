package com.sailing.rpc.server;

import com.alipay.remoting.AsyncContext;
import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.AsyncUserProcessor;
import com.sailing.rpc.dto.AsyncRequest;
import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 该处理器只处理异步消息，不处理同步消息
 * @param <T>
 */
@Slf4j
@Service
public class BusinessAsyncProcessor<T> extends AsyncUserProcessor<T> {

    @Override
    public void handleRequest(BizContext bizContext, AsyncContext asyncContext, T t) {
        asynchronousHandlerRequest((AsyncRequest) t);
    }

    @Override
    public String interest() {
        return AsyncRequest.class.getName();
    }

    public void asynchronousHandlerRequest(AsyncRequest request) {
        log.info("异步RPC客户端请求报文:>{},请求对象长度{}"
                , request.getUrl()
                , request.getObj() instanceof ArrayList ? ((ArrayList)request.getObj()).size() : request.getObj());
        if (request == null){
            log.info("请求对象为null，服务器端无法处理.");
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
    }
}
