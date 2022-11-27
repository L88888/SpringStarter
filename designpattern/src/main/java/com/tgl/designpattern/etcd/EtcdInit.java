package com.tgl.designpattern.etcd;

import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.PutResponse;

import java.util.concurrent.ExecutionException;

/**
 * @program: spring-starter
 * @description: etcd初始化连接
 * @author: LIULEI-TGL
 * @create: 2021-08-22 21:24:
 **/
public interface EtcdInit {

    // String IP = "182.168.80.20";
    static final String IP = "127.0.0.1";

    /**
     * 新建key-value客户端实例
     * @return
     */
     KV getKVClient();

    /**
     * 查询指定键对应的值
     * @param key
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    String get(String key) throws ExecutionException, InterruptedException;

    /**
     * 创建键值对
     * @param key
     * @param value
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    PutResponse put(String key, String value) throws ExecutionException, InterruptedException;
}
