package com.sailing.tgl.test;

/**
 * @program: spring-starter
 * @description: ThreadLocal 线程之间数据隔离
 * @author: LIULEI
 * @create: 2021-05-14 13:12:
 **/
public class ThreadDataCache implements AutoCloseable {

    /**
     * ThreadLocal 上下文
     */
    static final ThreadLocal ctx = new ThreadLocal();

    public ThreadDataCache(Object dataCache){
        // 存储当前线程的缓存对象
        ctx.set(dataCache);
    }

    public static Object getDataCache(){
        return ctx.get();
    }

    /**
     * 当前线程处理完业务后，自动删除对应的线程缓存数据
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        System.out.println("concurrent threadlocal storge close");
        ctx.remove();
    }
}
