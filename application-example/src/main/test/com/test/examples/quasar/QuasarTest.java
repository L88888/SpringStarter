package com.test.examples.quasar;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.alibaba.fastjson.JSONObject;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @program: spring-starter
 * @description: java 协程应用，比线程更小的并发框架
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-03-19 21:48:
 **/
@Slf4j
public class QuasarTest {

    /**
     * 1、先做一个协程与Java线程的对比
     *    1.1、同时启动1000个线程与1000个协程,看下两种方式的内存占用情况
     *    1.2、同时对10000万个数字进行并发累加,看下两种方式的执行时间周期
     */

    /**
     * 专门给协程创建一个阻塞队列
     */
//    LinkedBlockingQueue<Fiber> resDataQueue = new LinkedBlockingQueue<Fiber>(40000);

    LinkedBlockingQueue<Object> resDataQueue = new LinkedBlockingQueue<Object>(40000);

    /**
     * 场景一
     * 模拟一个平均执行需要1s的应用
     * 使用线程池ExecutorService并发执行10000次线程与协程分别所需要的时间
     */
    private void threadComputingTime(){
        log.info("threadComputingTime::开始执行.");
        try {
            long startTime = System.currentTimeMillis();
            // 用来控制线程并行执行顺序
            CountDownLatch t1 = new CountDownLatch(10000);
            ExecutorService executorService = Executors.newCachedThreadPool();
            // 使用线程池的方式在创建并发线程
            for (int i = 0;i < 10000;i++){
                // 不需要获取返回值数据
                executorService.submit(()->{
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        t1.countDown();
                    }
                });
            }

            t1.await();
            // 计算执行时间周期
            long endTime = System.currentTimeMillis();
            log.info("thread 并发执行耗时:>{}",endTime - startTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("threadComputingTime::执行结束.");
    }

    /**
     * 场景一
     * 模拟一个平均执行需要1s的应用
     * 使用quasar Fiber并发执行10000次线程与协程分别所需要的时间
     */
    private void quasarComputingTime(){
        log.info("quasarComputingTime::开始执行.");
        try {
            long startTime = System.currentTimeMillis();
            CountDownLatch t1 = new CountDownLatch(10000);
            // 使用线程池的方式在创建并发线程
            for (int i = 0;i < 10000;i++){
                // 不需要获取返回值数据
                new Fiber<>(new SuspendableRunnable(){
                    @Override
                    public void run() throws SuspendExecution, InterruptedException {
                        Fiber.sleep(1000);
                        t1.countDown();
                    }
                }).start();
            }

            t1.await();
            // 计算执行时间周期
            long endTime = System.currentTimeMillis();
            log.info("quasar 并发执行耗时:>{}",endTime - startTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("quasarComputingTime::执行结束.");
    }

    /**
     * 场景二
     * 模拟一个多线程环境
     * 使用thread并发创建100万个线程所需要占用多少内存
     */
    private void computingThreadMemory(){
        int threadNum = 1000000;
        for (int i = 0;i < threadNum;i++){
            new Thread(()->{
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 场景二
     * 模拟一个多线程环境
     * 使用quasar并发创建100万个线程所需要占用多少内存
     */
    private void computingQuasarMemory(){
        try {
            log.info("quasar 开始创建100万个线程对象.");
            int threadNum = 1000000;
            List resData = new ArrayList(threadNum);
            for (int i = 0;i < threadNum;i++){
                String s1 = "hello Quasar" + i;
                Fiber t1 = new Fiber<>((SuspendableCallable<String>) ()->{
                    // 开始处理业务逻辑
//                   Fiber.sleep(100);
                   return s1;
               }).start();

                try {
                    resData.add(t1.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            log.info("quasar 已经创建完100万个线程对象.{}", resData.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * quasar协程的限流操作
     */
    private void computingQuasarCurrentlimit(){
        try {
            log.info("quasar 开始创建400个线程对象.");
            // 模拟上游采集的数据条目
            int threadNum = 4432;
            for (int i = 0;i < threadNum;i++){
                String s1 = "hello Quasar" + i;
                Fiber t1 = new Fiber<>((SuspendableCallable<String>) ()->{
                    // 开始处理业务逻辑
//                        m1();
                    resDataQueue.add(m1(s1));
                    return s1;
                }).start();
            }
            log.info("quasar 已经创建完400个线程对象.{}", resDataQueue.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String m1(String s1) throws SuspendExecution, InterruptedException {
//        log.info("run my m1.");
        // 限流
//        Fiber.sleep(1000);
        return s1;
    }

    private void m2() {
        log.info("处理数据m2:>");
        try {
            while (true){
                // 获取队列的长度
                int tempLen = resDataQueue.size();
                log.info("队列长度为:>{}", tempLen);
                if (tempLen > 0){
                    log.info("该阶段的队列长度为:>{}", tempLen);
                    m3(tempLen);
                }else {
                    log.info("积攒数据m2,一秒积攒2次.");
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("{}", e.getMessage());
        }
    }

    /**
     * 数据处理,数据组装并入库
     * @param offset 队列偏移量
     */
    private void m3(int offset){
        List resData = new ArrayList();
//        Fiber<String> resVal;
        Object resVal;
        for (int i = 0;i <= offset;i++){
            resVal = resDataQueue.poll();
//            log.info(":>>>{}",resVal);
            if (Objects.isNull(resVal)){
                continue;
            }
            resData.add(resVal);
        }
        // 待入库的容器大小为
        log.info("待入库的容器大小为:>{}", resData.size());
        resData.clear();
        log.info("数据入库完成清理容器数据集:>{}", resData.size());
    }

    /**
     * quasar 执行速度平均比thread要快一倍多
     */
//    @Test
    public void become(){
        threadComputingTime();
        quasarComputingTime();
    }

//    @Test
    public void becomeMemory(){
        computingQuasarMemory();
    }

//    @Test
    public void becomeThreadMemory(){
        computingThreadMemory();
    }

//    @Test
    public void computingQuasarCurrentlimitTest(){
        computingQuasarCurrentlimit();
        // 开始处理业务逻辑
//        m2();

        new Thread(()->{
           m2();
        }).start();
    }

    // 借助协程生产数据至kafka中
    public static void main(String[] agre){
        QuasarTest quasarTest = new QuasarTest();
        // 模拟消费数据
        new Thread(()->{
            log.info("开始处理业务数据.");
            quasarTest.m2();
        }).start();

        WebClientOptions options = new WebClientOptions()
                .setUserAgent("My-App/1.2.3");
        options.setKeepAlive(false);
//        Vertx vertx
        WebClient client = WebClient.create(Vertx.vertx(), options);
        client.postAbs("sss")
                .sendJson(JSONObject.parse(""))
                .onSuccess(res->{

                })
                .onFailure(fai->{

                });

        try {
            // 模拟生产数据
            while (true){
                quasarTest.computingQuasarCurrentlimit();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}