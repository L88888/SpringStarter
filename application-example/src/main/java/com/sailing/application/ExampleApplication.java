package com.sailing.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 统一启动入口
 */
@ComponentScan(value = {"com.sailing.*"})
@EnableFeignClients(value = {"com.sailing.*"})
@SpringBootApplication
@EnableAsync
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    /**
     * 一、并发从程序方面的理解
     * 1、一组在逻辑上互相独立的程序或业务代码片段，在执行过程中的时间是互相叠加的
     * 二、如何判断是否可以并发执行
     * 1、如果两个程序或多个程序之间存在前趋关系（上下依赖）需要顺序执行，如果不存在前趋关系则可以并发执行
     * @return
     */
    @Bean(name = "asyncTaskExecutor")
    public AsyncTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("LINKSTRACK-API");
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(100);
        // 每一个线程的存活时间
        threadPoolTaskExecutor.setKeepAliveSeconds(200);
        // 线程池队列满了之后需要做的后续操作
        threadPoolTaskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler(){

            /**
             * 自定义实现线程池队列满了之后的拒绝操作
             * 每隔两秒给线程池队列里头添加一次
             * @param r 线程对象
             * @param executor 线程池对象，执行方式
             */
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                boolean flage = false;
                while (!flage){
                    // 异常处理
                    try {
                        flage = executor.getQueue().add(r);
                        Thread.sleep(10);
                    } catch (Exception e) {
                        // 进不进去队列满了,queue full
                        e.printStackTrace();

                        try {
                            flage = false;
                            // 加不进去2秒后在加一次
                            Thread.sleep(2000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
        // 加载属性设置信息
        threadPoolTaskExecutor.afterPropertiesSet();

        return threadPoolTaskExecutor;
    }
}
