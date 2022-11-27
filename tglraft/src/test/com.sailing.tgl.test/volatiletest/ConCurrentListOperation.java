package com.sailing.tgl.test.volatiletest;

import com.tgl.raft.concurrent.TglRaftThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-19 19:06:
 **/
@Slf4j
public class ConCurrentListOperation {

    AtomicInteger atomicInteger = new AtomicInteger(0);
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
                atomicInteger.getAndIncrement();

                // 开始准备落库
                int tempData = atomicInteger.get();
                if (tempData > 0){
                    if (tempData % 10000 == 0){
                        atomicInteger.set(0);
                        System.out.println("开始save()落库操作<===>" + String.valueOf(System.currentTimeMillis()) + "<===>" + tempData);
                    }
                }
            }catch (Exception e){

            }finally {
                c.countDown();
            }
        });
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
}
