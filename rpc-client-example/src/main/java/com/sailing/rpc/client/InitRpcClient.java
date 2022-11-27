package com.sailing.rpc.client;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.sailing.rpc.dto.AsyncRequest;
import com.sailing.rpc.dto.Request;
import com.sailing.rpc.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description: raft 客户端给服务端通过rpc协议发送消息数据，中间的消息传输可以使用massagepack|pb|json进行序列化，反序列化操作
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:56:
 **/
@Slf4j
@Service("initRpcClient")
public class InitRpcClient implements BusinessRpcClient {

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

    /**
     * 客户端发送消息给服务器
     * @param requestData 消息对象
     * @return
     */
    @Override
    public Response send(Request requestData) throws RemotingException{
        return send(requestData, RaftUtils.getInstance().TIMEOUT);
    }

    @Override
    public Response send(Request requestData, int timeOut) throws RemotingException{
        Response response = null;
        try {
            // 同步调用
            response = (Response) RPC_CLIENT.invokeSync(requestData.getUrl()
                    , requestData
                    , timeOut == 0 ? 5000 : timeOut);
        } catch (RemotingException e) {
            log.info("synchronization RPC RemotingException().:>{}", e.fillInStackTrace());

            // 这里开始重试

            throw new RemotingException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void sendAsync(AsyncRequest requestData) throws RemotingException {

    }

    @Override
    public void sendAsync(AsyncRequest requestData, int timeOut) throws RemotingException {

    }
}
