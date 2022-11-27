package com.sailing.reflex.wait;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class CountDownLatchTest {

    private static void testExceptionV1(){
        Map<String, Object> map = Collections.synchronizedMap(new HashMap<>(2));
        ExecutorService pool = null;
        try {
            // 利用线程同时查询设备目录统计和目录下的设备
            pool = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024),
                    new ThreadFactoryBuilder().setNameFormat("设备树查询-%d").build(),
                    new ThreadPoolExecutor.AbortPolicy()
            );
            // 计数器
            CountDownLatch countDownLatch = new CountDownLatch(2);
            pool.execute(()-> {
                LocalDateTime arSt = LocalDateTime.now();
//                List<Map> areaList = deviceApiMapper.queryAllAreaVidByParentIdUserId(treeParams);
                LocalDateTime arEn = LocalDateTime.now();
                log.info("查询区域信息-所用时间{}ms", Duration.between(arSt, arEn).toMillis());
                Integer.parseInt("sssssssss");
                //根据区域查询下级区域
                map.put("area", "ssssssssssss");
                countDownLatch.countDown();
            });
            pool.execute(()-> {
                //根据区域用户查询视频设备
                map.put("device", "1234567890");
                countDownLatch.countDown();
            });
            countDownLatch.await();

            log.info("设备树查询-所用时间{}ms", map.toString());
        } catch (InterruptedException e) {
            log.error("线程异常:{}", e);
            Thread.currentThread().interrupt();
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
    }

    /**
     * submit模式
     */
    private static void testExceptionV2(){
        Map<String, Object> map = Collections.synchronizedMap(new HashMap<>(2));
        ExecutorService pool = null;
        try {
            // 利用线程同时查询设备目录统计和目录下的设备
            pool = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024),
                    new ThreadFactoryBuilder().setNameFormat("设备树查询-%d").build(),
                    new ThreadPoolExecutor.AbortPolicy()
            );
            Future res1 = pool.submit(()-> {
                String resData = "";
                try {
                    resData = getAreaData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return resData;
            });

            log.info("开始查询设备信息集合-所用时间{}ms");
            Future res2 = pool.submit(()-> {
                log.info("查询设备信息集合-所用时间{}ms");
                //根据区域用户查询视频设备
                return "area2";
            });
            map.put("area", res1.get());
            map.put("device", res2.get());
            log.info("设备树查询-所用时间{}ms", map.toString());
        } catch (Exception e) {
            log.error("线程异常:{}", e);
            // 线程中断执行
            Thread.currentThread().interrupt();
        } finally {
            if (pool != null) {
                pool.shutdown();
            }
        }
    }

    /**
     * 实现获取设备区域逻辑
     */
    @NotNull
    private static String getAreaData() throws InterruptedException {
        LocalDateTime arSt = LocalDateTime.now();
        LocalDateTime arEn = LocalDateTime.now();
        log.info("查询区域信息-所用时间{}ms", Duration.between(arSt, arEn).toMillis());
//        Integer.parseInt("sssssssss");
        Thread.sleep(5000);
        //根据区域查询下级区域
//                map.put("area", "ssssssssssss");
        return "area1CountNum";
    }

    public static void main(String[] args) {
        testExceptionV2();
    }
}


/**
 * 实现设备信息检索逻辑
 */
@Slf4j
class Device implements Callable<String>{

    @Override
    public String call() throws Exception {
        log.info("查询设备信息集合-所用时间{}ms");
        //根据区域用户查询视频设备
        return "area2";
    }
}
