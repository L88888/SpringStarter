//package com.tgl.designpattern.etcd;
//
//import io.etcd.jetcd.ByteSequence;
//import io.etcd.jetcd.Client;
//import io.etcd.jetcd.KV;
//import io.etcd.jetcd.kv.GetResponse;
//import io.etcd.jetcd.kv.PutResponse;
//import lombok.extern.slf4j.Slf4j;
//import java.models.concurrent.ExecutionException;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//
///**
// * @program: spring-starter
// * @description:
// * @author: LIULEI-TGL
// * @create: 2021-08-22 21:29:
// **/
//@Slf4j
//public class EtcdInitImpl implements EtcdInit {
//
//    /**
//     * 新建key-value客户端实例
//     * @return
//     */
//    public KV getKVClient(){
//        String endpoints = "http://" + IP + ":2379,http://" + IP + ":2380";
//        Client manager = Client.builder().endpoints(endpoints.split(",")).build();
//        return manager.getKVClient();
//    }
//
//    /**
//     * 将字符串转为客户端所需的ByteSequence实例
//     * @param val
//     * @return
//     */
//    private static ByteSequence bytesOf(String val) {
//        return ByteSequence.from(val, UTF_8);
//    }
//
//    /**
//     * 查询指定键对应的值
//     * @param key
//     * @return
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public String get(String key) throws ExecutionException, InterruptedException{
////        log.info("start get, key [{}]", key);
//        GetResponse response = getKVClient().get(bytesOf(key)).get();
//
//        if (response.getKvs().isEmpty()) {
////            log.error("empty value of key [{}]", key);
//            return null;
//        }
//
//        String value = response.getKvs().get(0).getValue().toString(UTF_8);
////        log.info("finish get, key [{}], value [{}]", key, value);
//        return value;
//    }
//
//    /**
//     * 创建键值对
//     * @param key
//     * @param value
//     * @return
//     * @throws ExecutionException
//     * @throws InterruptedException
//     */
//    public PutResponse put(String key, String value) throws ExecutionException, InterruptedException {
//        log.info("start put, key [{}], value [{}]", key, value);
//        return getKVClient().put(bytesOf(key), bytesOf(value)).get();
//    }
//}
