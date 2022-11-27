package com.sailing.tgl.tests;

import com.tgl.rdbms.concurrent.TglRaftThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @program: spring-starter
 * @description: 状态机模块测试
 * @author: LIULEI-TGL
 * @create: 2021-05-31 12:12:
 **/
@Slf4j
public class StateMachineTest {

    /**
     * todo 初始化db文件父目录
     */
    static {
        System.setProperty("clientPort","1314");
    }

    /**
     * 测试状态机管理(key/value)
     */
//    @Test
////    public void testStsteMachine(){
////        StateMachine stateMachine = DefaultStateMachineImpl.getInstance();
////        // 号牌号码_号牌颜色(枚举)
////        String bussKey = "陕A12312_05";
////        Command command = Command.newBuilder()
////                .key(bussKey)
////                .value("陕A88888 过车基本信息。")
////                .build();
////
////        // 刷新前校验状态机里头的key存在否?
////        // todo 使用状态机来判断是否需要给增加日志条目，如果状态机中有车辆保有量数据。那么日志条目与状态机就不需要再次增加该记录
////        LogEntry isexistLog = stateMachine.get(bussKey);
////        if (!Objects.isNull(isexistLog.getCommand())){
////            log.warn("号牌号码已存在保有量缓存中，无需重复存储.{}", isexistLog);
////            return;
////        }
////
////        // 封装日志条目
////        LogEntry logEntry = LogEntry.newBuilder()
////                .term(1)
////                .command(command)
////                .build();
////        // 过车信息刷新到日志条目中
////        LogModule logModule = DefaultLogEntryImpl.getInstance();
////        logModule.write(logEntry);
////        // 过车信息刷新到状态机(保有量中)
////        // todo 状态机必须与日志条目一起存储执行
////        stateMachine.apply(logEntry);
////
////        logEntry = stateMachine.get(bussKey);
////        log.info(">>>{}" +  logEntry);
////
////        String bussData = stateMachine.getString(bussKey);
////        log.info(">>>{}" + bussData);
////
////        String resData = stateMachine.getCommandData(bussKey);
////        log.info(">>>{}" + resData);
////
////        Command commandData = stateMachine.getCommandInfo(bussKey);
////        log.info(">>>{}" + commandData);
////    }
////
////    /**
////     * 测试日志条目logentry
////     */
////    @Test
////    public void testLogEntry(){
////        LogModule logModule = DefaultLogEntryImpl.getInstance();
//////        logModule.removeOnStartIndex(0L);
////
////        log.info(">>>{}",logModule.getLastIndex());
////        // 遍历日志条目数据,挨个获取日志中的车辆保有量数据
////        LogEntry logEntry = null;
////        for (long i = 0 ;i<=logModule.getLastIndex();i++){
////            logEntry = logModule.read(i);
////            log.info("车辆保有量数据:>{}", logEntry);
////        }
////    }

    @Test
    public void conCurrntBag(){
//        List tempData = new FastList(Integer.class);
        List<Integer> tempData = new CopyOnWriteArrayList();
//        List<Integer> tempData = new ArrayList();// 不支持循环操作内存链表
        for (int i =0;i<= 100000 ;i++){
            tempData.add(i);
        }
        long start = System.currentTimeMillis();
        log.info("对象信息开始输出。");
        CountDownLatch c = new CountDownLatch(tempData.size());

        for (int o : tempData){
            TglRaftThreadPool.execute(()->{
                try{
                    if (o % 2 == 0){
                        tempData.remove(o);
                    }else {
//                        if ((int)o % 9 == 0){
//                            log.info("输出对象值:>{}", o);
//                        }
                        System.out.println("输出对象值:>{}" + o);
                    }
                }catch (Exception e){

                }finally {
                    c.countDown();
                }
            });
        }

        if (false){
            // 不支持
            tempData.parallelStream().forEach((o)->{
                try{
                    if ((int)o % 2 == 0){
                        tempData.remove(o);
                    }else {
//                        log.info("输出对象值:>{}", o);
                        System.out.println("输出对象值:>{}" + o);
                    }
                }catch (Exception e){

                }finally {
                    c.countDown();
                }
            });
        }

        log.info("对象信息输出完成。");
        try {
            if (true){
                c.await(1000, TimeUnit.MILLISECONDS);
            }
            System.out.println(System.currentTimeMillis() - start);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
