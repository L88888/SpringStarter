package com.sailing.tgl.test;

import com.tgl.raftclient.concurrent.TglRaftThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-19 19:06:
 **/
@Slf4j
public class ConCurrentListOperation {

    AtomicInteger atomicInteger = new AtomicInteger(1);
    CountDownLatch c = new CountDownLatch(100000);
    /// 并发情况下操作集合对象，删除元素对象
    public void dataOperation(Object o){
        TglRaftThreadPool.execute(()->{
            try{
//                if ((int)o % 2 == 0){
//                    tempData.remove(o);
//                }else {
////                        log.info("输出对象值:>{}", o);
//                }
                // 重点人员业务逻辑处理
                // get redis
//                System.out.println("输出对象值:>{}" + o);
//                atomicInteger.getAndIncrement();
//
//                // 开始准备落库
//                int tempData = atomicInteger.get();
//                if (tempData > 0){
//                }
                int tempData = atomicInteger.incrementAndGet();
                if (tempData % 10000 == 0){
                    System.out.println("开始save()落库操作<===>" +
                            String.valueOf(System.currentTimeMillis()) + "<===>" + tempData);
                    atomicInteger.set(0);
                }
            }catch (Exception e){

            }finally {
//                c.countDown();
            }
        });

//        try {
//            c.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 解析excel重点人员数据
     */
    @Test
    public void dispectData(){
        List tempData = new CopyOnWriteArrayList();
        for (int i =1;i<= 100000 ;i++){
            tempData.add(i);
        }
        long start = System.currentTimeMillis();

        for (Object o : tempData) {
            dataOperation(o);
        }
    }

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;

    /**
     * 采用syncAndReturnAll()解决sync()关闭管道前获取get()数据的问题。
     */
    @Test
    public void redisTest(){
        Jedis jedis = null;
        try {
            jedis = new Jedis(HOST, PORT);
            Pipeline pipelined = jedis.pipelined();
            String keyPrefix = "pipeline";

            for (int i = 1; i < 10000; i++) {
                String key = keyPrefix + "_" + i;
                pipelined.get(key);
                log.info(">:{}", pipelined.syncAndReturnAll());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    @Test
    public void batchSetUsePipeline() {
        Jedis jedis = new Jedis(HOST, PORT);
        Pipeline pipelined = jedis.pipelined();
        String keyPrefix = "pipeline";
        long begin = System.currentTimeMillis();
        for (int i = 1; i < 10000; i++) {
            String key = keyPrefix + "_" + i;
            String value = String.valueOf(i);
            pipelined.set(key, value);
        }
        pipelined.sync();
        jedis.close();
        long end = System.currentTimeMillis();
        System.out.println("use pipeline batch set total time：" + (end - begin));
    }

    public void dataBase64(String text){
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            final Base64.Encoder encoder = Base64.getEncoder();
            final byte[] textByte = text.getBytes("UTF-8");
            //编码
            final String encodedText = encoder.encodeToString(textByte);
            System.out.println(encodedText);
            //解码
            System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDataBase64(){
        dataBase64("013697");
        dataBase64("zyga2020");

        dataBase64("MDEzNjk3");
        dataBase64("enlnYTIwMjA=");

//        MDEzNjk3
//        013697
//        enlnYTIwMjA=
//                zyga2020
//        TURFek5qazM=
//                MDEzNjk3
//        ZW5sbllUSXdNakE9
//                enlnYTIwMjA=

//        TURFek5qazM=
//                ZW5sbllUSXdNakE9
    }


    AtomicInteger juNum = new AtomicInteger();
    @Test
    public void testNum(){
        try {
            Thread[] threads = new Thread[100];
            CountDownLatch count = new CountDownLatch(threads.length);
            for (int y =0;y< threads.length;y++){
                threads[y] = new Thread(()->{
                    for (int i =0;i< 10000;i++){
                        juNum.incrementAndGet();
                    }
                    count.countDown();
                });
            }
            Arrays.stream(threads).forEach(t->t.start());
            // 等待计数器跑完为止，否则一直等待
            count.await();
            log.info("最终计数器大小为:>{}",juNum.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
