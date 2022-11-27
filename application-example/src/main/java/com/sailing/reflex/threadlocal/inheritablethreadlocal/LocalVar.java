package com.sailing.reflex.threadlocal.inheritablethreadlocal;

import com.sailing.reflex.threadlocal.bo.Stu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: spring-starter
 * @description: 多线程共享本地变量
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-06-04 10:06:
 **/
public class LocalVar {

    // 主线程变量
    private ThreadLocal<String> masterData = new ThreadLocal<>();

    // 主线程定义数据给子线程中传递下去，初始化时定义完成（不能动态定义）
    // 主子线程间数据不隔离
//    private ThreadLocal<Stu> masterData1 = new InheritableThreadLocal<>();

    // 主子线程之间数据完全隔离，互相不受影响
    private ThreadLocal<Stu> masterData1 = new MyInheritableThreadLocal();

    // 线程池大小为1，保证线程提交前后是一个
    private ExecutorService localThreadPool = Executors.newFixedThreadPool(1);

    private void getMasterData(){
        // 给主线程赋值
        masterData.set("www.baidu.com");
        System.out.println("getMasterData()::主线程初始化的数据:>>>" + masterData.get());

        localThreadPool.execute(()->{
            System.out.println("getMasterData()::子线程中得到的数据:>>>" + masterData.get());
        });
    }

    private void getMasterDataTo(){
        Stu stu = new Stu("张三","12","西安市大寨路1230号.");
        // 给主线程赋值
        masterData1.set(stu);

        // 先提交一个线程,修改人员年龄属性
        localThreadPool.submit(()->{
            System.out.println("getMasterDataTo()::子线程读取主线程本地变量数据:>>>" + masterData1.get());
            masterData1.get().setSex("33");
            System.out.println("getMasterDataTo()::子线程修改stu对象属性后读取本地变量数据:>>>" + masterData1.get());
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("getMasterDataTo()::主线程读取本地变量(读取到了子线程本地变量):>>>" + masterData1.get());
        masterData1.get().setSex("123");
        System.out.println("getMasterDataTo()::主线程读取本地变量:>>>" + masterData1.get());

        // 在提交一个线程,查看主线程修改后的数据有没有共享到子线程中
        localThreadPool.execute(()->{
            System.out.println("getMasterDataTo()::子线程读取本地变量(读取到了主线程本地变量):>>>" + masterData1.get());
        });
    }

    public static void main(String[] arge){
        LocalVar t1 = new LocalVar();
        if (false){
            t1.getMasterData();
        }

        t1.getMasterDataTo();
    }
}