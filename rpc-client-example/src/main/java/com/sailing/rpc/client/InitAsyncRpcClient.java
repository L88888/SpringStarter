package com.sailing.rpc.client;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.sailing.rpc.dto.AsyncRequest;
import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("initAsyncRpcClient")
public class InitAsyncRpcClient implements BusinessRpcClient {
    /**
     * 初始化rpcclient 客户端;启动时就装载好
     */
    private static final RpcClient RPC_CLIENT = new RpcClient();

    /**
     * 静态初始化
     */
    static {
        RPC_CLIENT.startup();
    }

    @Override
    public void sendAsync(AsyncRequest requestData) throws RemotingException {
        sendAsync(requestData, RaftUtils.getInstance().TIMEOUT);
    }

    @Override
    public void sendAsync(AsyncRequest requestData, int timeOut) throws RemotingException {
        try {
            // 异步调用
            RPC_CLIENT.invokeWithFuture(requestData.getUrl()
                    , requestData
                    , timeOut == 0 ? 5000 : timeOut);
        } catch (RemotingException e) {
            log.info("AsyncRequest RPC RemotingException().:>{}", e.fillInStackTrace());
            throw new RemotingException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端发送消息给服务器
     * @param requestData 消息对象
     * @return
     */
    @Override
    public Response send(Request requestData) throws RemotingException{
        return new Response(null);
    }

    @Override
    public Response send(Request requestData, int timeOut) throws RemotingException{
        return new Response(null);
    }
}
