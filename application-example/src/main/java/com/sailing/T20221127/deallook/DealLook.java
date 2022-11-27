package com.sailing.T20221127.deallook;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DealLook
 * @Description 多线程死锁的场景以及解决方案
 * @Author Liulei
 * @Date 2022/11/27 12:27
 * @Version 1.0
 **/
@Slf4j
public class DealLook {

    // 构造静态死锁对象
    Object name1 = new Object();
    Object name2 = new Object();

    private void f1(){
        synchronized (name1){
            log.info("", );
        }
    }

}
