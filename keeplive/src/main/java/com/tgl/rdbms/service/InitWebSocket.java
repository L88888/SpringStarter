package com.tgl.rdbms.service;

import com.tgl.rdbms.ws.RealHeartBeat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: spring-starter
 * @description: 初始化websocket对象集合
 * @author: LIULEI-TGL
 * @create: 2021-07-13 18:50:
 **/
public class InitWebSocket {

    private volatile static InitWebSocket INITWEBSOCKET;

    /**
     * 存储所有的客户端请求websocket对象
     * key：登录人的token字符串
     * value: realHeartBeat对象
     */
    private static Map<String, RealHeartBeat> webSocketSet;

    /**
     * 记录每一个token的失败次数，失败次数达到三次直接发送注销token操作
     * key: 登录人的token字符串
     * value: 失败的次数累计
     */
    private static Map<String, Integer> fail;

    private InitWebSocket(){}

    public static InitWebSocket getInstance(){
        if (INITWEBSOCKET == null){
            synchronized (InitWebSocket.class){
                if (INITWEBSOCKET == null){
                    INITWEBSOCKET = new InitWebSocket();
                    webSocketSet = new ConcurrentHashMap<String, RealHeartBeat>();
                    fail = new ConcurrentHashMap<String, Integer>();
                }
            }
        }
        return INITWEBSOCKET;
    }

    public boolean setWebSocket(String token, RealHeartBeat realHeartBeat){
        this.webSocketSet.put(token, realHeartBeat);
        return true;
    }

    public Map<String, RealHeartBeat> getWebSocket(){
        return this.webSocketSet;
    }

    public void remove(String token){
        this.webSocketSet.remove(token);
    }

    public boolean setFail(String token, Integer failNum){
        this.fail.put(token, failNum);
        return true;
    }

    public Map<String, Integer> getFail(){
        return this.fail;
    }

    public void removeFail(String token){
        this.fail.remove(token);
    }
}
