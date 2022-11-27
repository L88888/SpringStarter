package com.tgl.raftclient;

import okhttp3.*;

import java.io.IOException;

/**
 * @program: spring-starter
 * @description: 定义程序中所需要的各种公共方法、变量等
 * @author: LIULEI-TGL
 * @create: 2021-05-23 15:02:
 **/
public class RaftUtils {

    private RaftUtils(){}

    private volatile static RaftUtils RAFTUTILS;

    /**
     * 创建单例对象
     * @return
     */
    public static RaftUtils getInstance(){
        if (RAFTUTILS == null){
            synchronized (RaftUtils.class){
                if (RAFTUTILS == null){
                    RAFTUTILS = new RaftUtils();
                }
            }
        }
        return RAFTUTILS;
    }

    /**
     * 客户端发送消息至服务器链路超时时间。默认：3分钟
     */
    public final int TIMEOUT = 200000;

    /**
     * 用于在日志下标转换时使用
     * @param l
     * @return
     */
    public final long convert(Long l){
        if (l == null){
            return 0;
        }
        return 1;
    }

    /**
     * 获取db中最后下标值的key 字节数据
     * @return
     */
    public final byte[] getLastIndexKey(){
        return "LAST_INDEX_KEY".getBytes();
    }

    /**
     * 获取0下标值的字节数据
     * @return
     */
    public final byte[] getZeroIndexKey(){
        return "0".getBytes();
    }

    public final byte[] getInvalidIndexKey(){
        return "-1".getBytes();
    }

    public final byte[] getIndexConvert(Long index){
        return index.toString().getBytes();
    }

    public void okhttpTest(){
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://httpbin.org/get")
                    .build();

            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
