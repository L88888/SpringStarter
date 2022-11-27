package com.sailing.paralleltask.poolutil;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @ClassName SitTaskProcess
 * @Description 线程池定义、获取、以及资源回收
 * @Author Liulei
 * @Date 2022/11/24 21:14
 * @Version 1.0
 **/
public class SitTaskProcess {

    /**
     * 多任务缓存，一个任务对应一个线程池对象ExecutorService
     */
    private static Map<String, ExecutorService> taskPools = new ConcurrentHashMap<>(200);

    /**
     * 动态初始化线程池
     * @param poolName 线程池名称
     * @param poolSize 线程池大小
     * @return ExecutorService (new ThreadPoolExecutor)
     */
    private static ExecutorService dynamicInitPool(String poolName, int poolSize){
        if (Objects.isNull(poolName)){
            throw new RuntimeException("未定义线程池名称.");
        }

        if (poolSize <= 0){
            throw new RuntimeException("线程池大小不合法");
        }

        // 自定义创建线程池对象
        ThreadPoolExecutor customThreadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactoryBuilder()
                        .setNameFormat("pool-[" + poolName + "]")
                        .setDaemon(false)
                        .build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return customThreadPoolExecutor;
    }

    /**
     * 获取线程池对象，或者初始化线程池对象
     * @param poolName 线程池名称
     * @param poolSize 线程池大小
     * @return ExecutorService (new ThreadPoolExecutor)
     */
    public static ExecutorService getOrInitExecutor(String poolName, int poolSize){
        if (Objects.isNull(poolName)){
            throw new RuntimeException("未定义线程池名称.");
        }

        ExecutorService executorService = taskPools.get(poolName);
        if (Objects.isNull(executorService)){
            synchronized (SitTaskProcess.class){
                executorService = taskPools.get(poolName);
                if (Objects.isNull(executorService)){
                    executorService = dynamicInitPool(poolName, poolSize);
                    taskPools.put(poolName, executorService);
                }
            }
        }
        return executorService;
    }

    /**
     * 线程池用完之后，进行销毁
     * @param poolName 线程池名称
     */
    public static void recoveryExecutor(String poolName){
        if (Objects.isNull(poolName)){
            throw new RuntimeException("未定义线程池名称.");
        }

        ExecutorService executorService = taskPools.remove(poolName);
        if (Objects.nonNull(executorService)){
            executorService.shutdown();
        }
    }

    /**
     * 初始化创建线程池对象
     * @param poolName 线程池名称
     * @param poolSize 线程池大小
     * @return 线程池对象ExecutorService
     */
    public static ExecutorService initPool(String poolName, int poolSize){
        return dynamicInitPool(poolName, poolSize);
    }
}