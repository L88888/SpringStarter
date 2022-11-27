package com.sailing.tgl.tests;

import org.junit.Test;
import sun.misc.Contended;

/**
 * @program: spring-starter
 * @description: 缓存行(cacheline)
 *  缓存对其
 * @author: LIULEI-TGL
 * @create: 2021-09-01 16:47:
 **/
public class CacheLine {

    @Contended
    volatile long a;
    @Contended
    volatile long b;
    @Contended
    volatile long c;
    @Contended
    volatile long d;

    @Test
    public void testContended() throws InterruptedException {
        CacheLine mqTest = new CacheLine();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000_0000L; i++) {
                mqTest.a = i;
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000_0000L; i++) {
                mqTest.b = i;
            }
        });
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 10000_0000L; i++) {
                mqTest.c = i;
            }
        });
        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < 10000_0000L; i++) {
                mqTest.d = i;
            }
        });
        Long start = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        System.out.println(System.currentTimeMillis() - start);
    }
}
