package com.test.examples.dispeldoubts;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: spring-starter
 * @description: 并发编程,之序列化执行
 * 《JAVA并发编程实战》
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-03-06 15:37:
 **/
public class ConCurrent1 {
    static int t;
    static AtomicInteger atomicIntege = new AtomicInteger(0);

//    AtomicReference atomicReference = new AtomicReference();

    public static void main(String[] args){
        t++;

        String s1 = "sdsdwesds啊单位请问";
        System.out.println("String 对象自己的hashcode数据:>" + s1.hashCode());

        int s1_hash_code =
                System.identityHashCode("System 对象的hashcode数据:>" + s1);
        System.out.println("String 对象自己的hashcode数据:>" + s1_hash_code);
    }
}