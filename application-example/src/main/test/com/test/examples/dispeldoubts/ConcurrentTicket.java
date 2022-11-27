package com.test.examples.dispeldoubts;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: 两个线程同时并发抢票
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-03-06 16:38:两个线程同时并发抢票
 **/
public class ConcurrentTicket {

    /**
     * 设计两个线程，同时进行车票的购买
     * 1、车票数量10张;
     * 2、线程数量2;
     * 3、每张票只能有一个人抢到;
     */
    public static void main(String[] args){
       // 创建一个线程组
        if (true){
            Thread[] grabbingThread = new Thread[2];
            grabbingThread[0] = new Thread(new TicketCosumer(10),"康熙");
            grabbingThread[1] = new Thread(new TicketCosumer(10),"乾隆");

            // 开始启动线程grabbingThread对象
            Arrays.asList(grabbingThread).forEach((t)->t.start());
        }
    }
}

/**
 * 车票消费端逻辑处理
 * 一共有10张票,车票是一个共享资源ticket.有两个线程来同时消费,每张票同时只能被其中的一个人所获取。
 */
class TicketCosumer implements Runnable{

    // 存储车票资源
    // integer对象是int数据类型的包装对象,在每次创建时都会进行new操作
    // 即便是相同的一个数字值，例如10 也会进行new操作
    private volatile static Integer ticketNum;

    /**
     * 创建构造器接收车票数量
     */
    public TicketCosumer(Integer ticketNum){
        this.ticketNum = ticketNum;
    }

    /**
     * 开始循环售票
     */
    @Override
    public void run() {
        while (true){
            System.out.println(Thread.currentThread().getName()+"开始抢第"+ticketNum+"张票," +
                    "对象枷锁之前hashcode:["+ System.identityHashCode(ticketNum) +"]." +
                    System.currentTimeMillis());
            synchronized (CacheSyncObject.getCacheSyncObject(ticketNum)){
            /*synchronized (TicketCosumer.class){*/
                if (ticketNum > 0){
                    System.out.println("*****" + Thread.currentThread().getName()+"抢到第"+ticketNum+"张票," +
                            "成功锁到的对象hashcode:["+ System.identityHashCode(ticketNum) +"]." +
                            System.currentTimeMillis() + "*****");
                    // 模拟抢票过程,程序这块暂停1秒
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    ticketNum--;
                    // ticketNum-- 相当于 ticketNum = new Integer(ticketNum--);
                    System.out.println("目前剩余车票["+ ticketNum-- +"]张." + System.currentTimeMillis());
                }else {
                    System.out.println("车票已经全部售卖完毕,目前剩余车票["+ ticketNum +"]张,程序退出." + System.currentTimeMillis());
                    return;
                }
            }
        }
    }
}

class CacheSyncObject{
    private static ConcurrentMap<Integer, Object> locks =
            new ConcurrentHashMap<Integer, Object>(10);

    /**
     * 从缓存中获取包装类对象Integer
     * putIfAbsent()保证其相同数值不管new Integer()多少次拿到的都是第一次的对象引用
     * @return
     */
    public static Object getCacheSyncObject(final Integer val){
        // putIfAbsent操作当key存在时不会更新value数据
        locks.putIfAbsent(val, val);
        // put操作不管key是否存在每次都会进行覆盖value
//        lock.put(val, val);
        return locks.get(val);
    }
}