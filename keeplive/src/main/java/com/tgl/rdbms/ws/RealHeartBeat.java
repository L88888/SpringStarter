package com.tgl.rdbms.ws;

import com.tgl.rdbms.feign.IpassFeign;
import com.tgl.rdbms.service.InitWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: spring-starter
 * @description: 实时心跳推送服务,检测在线用户状态
 * @author: LIULEI-TGL
 * @create: 2021-07-13 09:15:
 **/
@ServerEndpoint(value = "/sit/RealHeartBeat")
@Component
@Slf4j
public class RealHeartBeat {

    /////////////////////////////////////////////////////////////////////////////////
    // 1、缓存所有的客户端连接；
    // 2、定时任务1秒刷新一次心跳状态推送至前端；发送成功后写入rocksdb中（整个ws连接对象都写进去）
    // 3、记录推送失败的次数，连续三次失败存储至失败队列中；
    // 4、定时任务1秒读取一次失败队列数据，发送token至ipass平台进行token注销//
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * alive
     */
    String message = "alive";

    @Autowired
    private IpassFeign ipassFeign;

    /**
     * 连接建立成功调用的方法
     * ws://127.0.0.1:2002/sit/RealHeartBeat?token=1231231231242132qwxzcsd
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            this.session = session;
            // 后期用于广播消息数据
            String pathParameters = session.getQueryString();
            pathParameters = pathParameters.replace("token=","");
            log.info("连接成功，sessionID:>{}", pathParameters);

            if (!Objects.isNull(pathParameters)){
                // 让token跟session绑定起来
                InitWebSocket.getInstance().setWebSocket(String.valueOf(pathParameters),this);
                InitWebSocket.getInstance().setFail(String.valueOf(pathParameters), 0);
                log.info("连接成功，sessionID={}",InitWebSocket.getInstance().getWebSocket().size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("websocket已关闭");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("接收到的客户端消息对象为:>{},{}", message, session);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.debug("websocket连接异常,发生错误{}",error.fillInStackTrace());
    }

    /**
     * 定时任务,遍历消息广播
     */
    AtomicInteger logNum = new AtomicInteger(0);
    @Scheduled(cron = "*/1 * * * * ?")
    public void sendInfo() throws IOException {
        if (Objects.isNull(InitWebSocket.getInstance().getWebSocket().isEmpty()) ||
                InitWebSocket.getInstance().getWebSocket().size() == 0) {
            if (logNum.incrementAndGet() % 50 == 0){
                log.info("暂无可用的websocket对象信息：>{}", InitWebSocket.getInstance().getWebSocket().size());
            }
            return;
        }

        InitWebSocket.getInstance().getWebSocket().forEach((k, v) -> {
            try {
                if (logNum.incrementAndGet() % 50 == 0){
                    log.info("token is value data:>{}", k);
                }
                v.sendMessage(v.session, message);
                updateSuccess(k);
            } catch (IOException e) {
                updateFail(k);
                log.info("客户端ws消息广播失败,失败信息为:>{},:>{}", k, e.getMessage());
            }
        });
    }

    /**
     * 实现服务器主动推送，每次直接广播
     */
    public void sendMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    /**
     * 定时任务,遍历失败的客户端ws连接
     */
    AtomicInteger fialLogNum = new AtomicInteger(0);
    @Scheduled(cron = "*/1 * * * * ?")
    private void jobTaskProcessFail(){
        if (Objects.isNull(InitWebSocket.getInstance().getFail().isEmpty()) ||
                InitWebSocket.getInstance().getFail().size() == 0) {
            if (fialLogNum.incrementAndGet() % 50 == 0){
                log.info("暂无失败的websocket对象信息：>{}", InitWebSocket.getInstance().getFail().size());
            }
            return;
        }

        InitWebSocket.getInstance().getFail().forEach((k, v) -> {
            try {
                if (v >= 3){
                    log.info("注销客户端ws请求对象:>{},:>{}", k, v);
                    // 开始发送token注销请求
                    ipassFeign.ajaxLogout(k);
                    // 发送完了之后删除缓存中的websocket对象
                    InitWebSocket.getInstance().remove(k);
                    InitWebSocket.getInstance().removeFail(k);
                }
            } catch (Exception e) {
                // 发送失败在发送一次
                log.info("注销客户端ws请求对象异常,异常堆栈为:>{},:>{},>:{}", k, v, e.getMessage());
                try {
                    // 开始发送token注销请求
                    ipassFeign.ajaxLogout(k);
                    // 发送完了之后删除缓存中的websocket对象
                    InitWebSocket.getInstance().remove(k);
                    InitWebSocket.getInstance().removeFail(k);
                } catch (Exception e1) {
                    log.info("注销客户端ws请求对象异常,异常堆栈为:>{},:>{},>:{}", k, v, e1.getMessage());
                }
            }
        });
    }

    /**
     * 成功后更新状态
     * @param token
     */
    private void updateSuccess(String token){
        InitWebSocket.getInstance().setFail(token, 0);
    }

    /**
     * 失败后更新状态
     * @param token
     */
    volatile int failNum = 0;
    private void updateFail(String token){
        failNum = InitWebSocket.getInstance().getFail().get(token) + 1;
        InitWebSocket.getInstance().setFail(token, failNum);
    }
}