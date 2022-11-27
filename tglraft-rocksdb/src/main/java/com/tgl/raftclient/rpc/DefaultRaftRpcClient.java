package com.tgl.raftclient.rpc;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.tgl.raftclient.RaftUtils;
import com.tgl.raftclient.exption.RaftRemotingException;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: spring-starter
 * @description: raft 客户端给服务端通过rpc协议发送消息数据，中间的消息传输可以使用massagepack|pb|json进行序列化，反序列化操作
 * @author: LIULEI-TGL
 * @create: 2021-05-21 21:56:
 **/
@Slf4j
public class DefaultRaftRpcClient implements RaftRpcClient {

    /** 初始化rpcclient 客户端;启动时就装载好 */
    private static final RpcClient RPC_CLIENT = new RpcClient();
    /** 静态初始化 */
    static {
        RPC_CLIENT.init();
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
            response = (Response) RPC_CLIENT.invokeSync(requestData.getUrl()
                    , requestData
                    , timeOut == 0 ? 5000 : timeOut);
        } catch (RemotingException e) {
            log.info("RPC raftRemotingException().:>{}", e.fillInStackTrace());
            throw new RaftRemotingException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
}
