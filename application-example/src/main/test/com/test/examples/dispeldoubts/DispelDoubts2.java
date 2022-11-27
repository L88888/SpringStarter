package com.test.examples.dispeldoubts;

import org.junit.Test;

/**
 * @program: spring-starter
 * @description: 服务停止时关闭hook（勾子）操作
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-01-20 17:04:
 **/
public class DispelDoubts2 {

    /**
     * 终结器关闭之前执行对应的输出
     */
    private void closeHook(){
        System.out.println("你好2021。");

        Runtime.getRuntime().addShutdownHook(
                new Thread(){
                    public void run(){
                        System.out.println("程序在退出时做出的一些控制。");

                        System.out.println("再见了2021。");

                        System.out.println("你好2022。");
                    }
                }
        );

        System.exit(0);
    }

    @Test
    public void main(){
        this.closeHook();
    }
}