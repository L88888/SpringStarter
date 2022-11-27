package com.sailing.tgl.test.volatiletest;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: spring-starter
 * @description: 验证Volatile轻量级锁的原子性操作
 * @author: LIULEI
 * @create: 2021-05-16 17:06:
 **/
public class VolatileTest {

    // 多线程的模式下对inc变量进行累加，使用三种不同的方式验证inc的变量最终值
    private volatile int inc = 0;

    /**
     * 多线程下变量自加
     */
    public void getIncVal(){
        for (int y =0;y < 1000;y++){
            this.inc++;
        }

        System.out.println("concurrent Thread inc data is value:>>>" + this.inc);
    }

    @Test
    public void increase(){
        for (int i =0;i<10; i++){
            new Thread(()->this.getIncVal(),("线程" + i)).start();
        }
    }

    @Test
    public void testAtomicInteger(){
        AtomicIncTest atomicInteger = new AtomicIncTest();
        atomicInteger.ConCurrentProcess();
    }
}

/**
 * 使用AtomicInteger保证变量累加原子性
 */
class AtomicIncTest{

    private AtomicInteger inc = new AtomicInteger();

    /**
     * 多线程下变量自加
     */
    private void getIntVal(){
        for (int y = 0; y < 1000;y++){
            // 保证变量累加原子性
            this.inc.getAndIncrement();
        }
        System.out.println(this.inc);
    }

    public void ConCurrentProcess(){
        for (int z =0;z < 10;z++){
            new Thread(()->this.getIntVal(),"线程" + z).start();
        }
    }
}
