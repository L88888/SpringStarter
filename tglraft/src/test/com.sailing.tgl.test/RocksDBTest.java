package com.sailing.tgl.test;

import com.alibaba.fastjson.JSON;
import com.alipay.remoting.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.var;
import org.junit.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI
 * @create: 2021-05-10 20:18:
 **/
public class RocksDBTest {

    RocksDB machineDb;
    static {
        // 加载rocksdb存放目录
        RocksDB.loadLibrary();
    }

    // 通过该key获取最新的下标数据
    public byte[] lastIndexKey = "LAST_INDEX_KEY".getBytes();

    // 使用ReentrantLock可重入锁，解决多线程并发写入的问题。加锁、释放锁很灵活
    ReentrantLock lock = new ReentrantLock();

    // 存储业务数据，防止多线程之间数据对象互串
    static ThreadLocal<Cmd> threadLocal = new ThreadLocal<>();

    /**
     * 初始化rocksdb环境配置
     * 1、目录；2、db文件名称;3、打开rocksdb
     */
    public RocksDBTest(){
        try {
            // 设置rocksdb存放目录
            System.setProperty("rocksdbPath","F:/rdbms-test/20211/test");
            // 获取客户端端口名称,通过端口来确定Bucket(水桶名称)
            String clientPort = System.getProperty("clientPort");
            System.out.println("bucket>>" + clientPort);
            Options var1 = new Options();
            var1.setCreateIfMissing(true);
            machineDb = RocksDB.open(var1,
                    StringUtils.isBlank(clientPort) ?
                            System.getProperty("rocksdbPath") :
                            System.getProperty("rocksdbPath") + clientPort);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询最新的消息索引下标
     * @return
     */
    private Long getLastIndex(){
        // 捕获rocksdb返回值
        byte[] lastIndex = new byte[0];
        try {
            lastIndex = machineDb.get(this.lastIndexKey);
            if (lastIndex == null){
                // 表示是初始化时期
                lastIndex = "0".getBytes();
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
//        System.out.println("【最新的下标值LastIndex】>>>" + Long.valueOf(new String(lastIndex)));
        return Long.valueOf(new String(lastIndex));
    }

    /**
     * 总集调度
     */
    private void dispaster(){
        try {
            try (var ctx = new ThreadDataCache(this.mockData())){
                this.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save(){
        boolean success = false;
        try {
            if (lock.tryLock(1000, MILLISECONDS)){
                Cmd userData = null;
                try {
                    // 开始加锁,停止500ms开始写入
                    // 使用公平锁,可以让并发的线程有序执行,但是过程中需要等待cpu获得线程对象
    //            lock.lock();
                    // 使用锁超时机制,如果指定时间内没有获得锁就去做其他的事情,可以有效解决多线程死锁的问题
                    System.out.println("获取当前线程名称>>" + Thread.currentThread().getName());
                    // 获取当前线程的ThreadLocal.Map对象值
                    userData = (Cmd) ThreadDataCache.getDataCache();
    //            System.out.println("----------------------" + userData);
                    if (userData != null){
    //                System.out.println("通过threadLocal来解决多线程之间数据隔离>>" + userData.toString());
                        userData.setIndex(getLastIndex() + 1);
                        machineDb.put(userData.getIndex().toString().getBytes(), JSON.toJSONBytes(userData));
    //            System.out.println("【保存业务数据成功】>>>" + userData);
                        success = true;
                    }
                } catch (RocksDBException e) {
                    e.printStackTrace();
                }finally {
                    if (success){
                        updateLastIndex(userData.getIndex());
                        // 整体流程处理完然后解锁
                        lock.unlock();
                    }
                }
            }else {
                System.out.println("【未获取到线程锁对象】");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 只存储带索引的消息对象
     * 防止多线程情况下引起的数据错乱以及不一致问题,使用
     * @param cmd
     */
    private void save(Cmd cmd){
        boolean success = false;
        try {
            // 开始加锁,停止500ms开始写入
            // 使用公平锁,可以让并发的线程有序执行,但是过程中需要等待cpu获得线程对象
//            lock.lock();
            // 使用锁超时机制,如果指定时间内没有获得锁就去做其他的事情,可以有效解决多线程死锁的问题
            lock.tryLock(2000, MILLISECONDS);
            System.out.println("获取当前线程名称>>" + Thread.currentThread().getName());
            // 获取当前线程的ThreadLocal.Map对象值
            Object userData = threadLocal.get();
            System.out.println(">>>>>>>>>>>>" + userData);
            if (userData != null){
                cmd = (Cmd) userData;
                System.out.println("通过threadLocal来解决多线程之间数据隔离>>" + cmd.toString());
                cmd.setIndex(getLastIndex() + 1);
                machineDb.put(cmd.getIndex().toString().getBytes(), JSON.toJSONBytes(cmd));
                System.out.println("【保存业务数据成功】>>>" + cmd);
                success = true;
            }
        } catch (RocksDBException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (success){
                updateLastIndex(cmd.getIndex());
            }
            // 整体流程处理完然后解锁
            lock.unlock();
        }
    }

    /**
     * 直接写入自定义消息结构对象
     * @param cmd
     */
    private void write(Cmd cmd){
        try {
            machineDb.put(cmd.getKey().toString().getBytes(), JSON.toJSONBytes(cmd));
            System.out.println("【保存业务数据成功】>>>" + cmd);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }finally {
            updateLastIndex(cmd.getIndex());
        }
    }

    /**
     * 每次创建完消息对象后，将LAST_INDEX_KEY值修改为最新的
     * @param lastIndex
     */
    private void updateLastIndex(Long lastIndex){
        try {
            machineDb.put(this.lastIndexKey, lastIndex.toString().getBytes());
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据下标获取Cmd对象
     * @param getLastIndex() 下标值
     */
    private Cmd getInfo(){
        Cmd resCmd = null;
        try {
            byte[] resData = this.machineDb.get(this.getLastIndex().toString().getBytes());
            if (resData != null && resData.length > 0){
                resCmd = JSON.parseObject(resData, Cmd.class);
                System.out.println("【获取到的消息对象值】>>>" + resCmd);
            }else {
                System.out.println("【未获取到的消息对象值】>>>" + resCmd);
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return resCmd;
    }

    @Getter
    @Setter
    @ToString
    static class Cmd{
        // 下标索引
        Long index;
        // key
        String key;
        // 业务数据
        String value;

        public Cmd(){}

        public Cmd(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 模拟打桩数据对象
     * @return
     */
    private Cmd mockData(){
        Cmd cmd = new Cmd();
        cmd.setKey("gtl3");

        cmd.setValue("{\n" +
                "  \"Data\": {\n" +
                "    \"ACCOUNT\": \"\",\n" +
                "    \"ACCOUNT_MODE\": \"4\",\n" +
                "    \"AP_CHANNEL\": \"11\",\n" +
                "    \"AP_ENCRYTYPE\": \"\",\n" +
                "    \"AREA_CODE\": \"\",\n" +
                "    \"BSSID\": \"77-77-77-77-77-77\",\n" +
                "    \"CITY_CODE\": \"\",\n" +
                "    \"COLLECT_SCENE\": \"9\",\n" +
                "    \"COMPANY_ID\": \"714666111\",\n" +
                "    \"CONSULT_XPOINT\": \"-1.0\",\n" +
                "    \"CONSULT_YPOINT\": \"-1.0\",\n" +
                "    \"DEVICENUM\": \"71000000001320000016\",\n" +
                "    \"DEVMAC\": \"77-77-77-77-77-77\",\n" +
                "    \"END_TIME\": \"20201119150930\",\n" +
                "    \"ESSID\": \"my-shunyi\",\n" +
                "    \"FLAG\": \"\",\n" +
                "    \"HISTORY_ESSID\": \"\",\n" +
                "    \"ID\": \"8f3f7c9afa1243f6a16c0d8787a5b479\",\n" +
                "    \"IMEI\": \"\",\n" +
                "    \"IMSI\": \"\",\n" +
                "    \"MAC\": \"77-77-77-77-77-77\",\n" +
                "    \"MODEL\": \"\",\n" +
                "    \"OS_VERSION\": \"\",\n" +
                "    \"PHONE\": \"\",\n" +
                "    \"POWER\": \"-54|-71\",\n" +
                "    \"PROTOCOL_TYPE\": \"\",\n" +
                "    \"SERVICECODE\": \"11011339001107\",\n" +
                "    \"START_TIME\": \"20201119090930\",\n" +
                "    \"STATION\": \"\",\n" +
                "    \"TYPE\": \"2\",\n" +
                "    \"URL\": \"\",\n" +
                "    \"XPOINT\": \"116.6518\",\n" +
                "    \"YPOINT\": \"40.1437\"\n" +
                "  },\n" +
                "  \"DataSource\": \"一所\",\n" +
                "  \"DataType\": \"TerminalFeature\",\n" +
                "  \"DeviceId\": \"8f3f7c9afa1243f6a16c0d8787a5b479\",\n" +
                "  \"DeviceModelID\": \"217974eef0a34edd95e75a2067e863e3\",\n" +
                "  \"DstDataId\": \"8f3f7c9afa1243f6a16c0d8787a5b479\",\n" +
                "  \"FailedType\": \"\",\n" +
                "  \"ProcessID\": \"11011300005030000001_6DC55182A2938FB1525ADAEACDE05768\",\n" +
                "  \"ServerID\": \"6DC55182A2938FB1525ADAEACDE05768\",\n" +
                "  \"SrcDataId\": \"8f3f7c9afa1243f6a16c0d8787a5b479\",\n" +
                "  \"SrcDataTime\": \"START_TIME\",\n" +
                "  \"VendorID\": \"dmxz\"\n" +
                "}");
        return cmd;
    }

    @Test
    public void test001(){
        // 创建bucket,8081
        System.setProperty("clientPort","8094");
        RocksDBTest rocksDBTest = new RocksDBTest();

        // 开启多线程,一次并发100个消息写入
        for (int i = 0;i < 30000;i++){
            new Thread(()->rocksDBTest.dispaster(),("线程" + i)).start();
        }

        if (false){
            Cmd cmd = new Cmd();
            cmd.setKey("gtl3");
            cmd.setValue("你好 rdbms.");
            rocksDBTest.save(cmd);
            rocksDBTest.write(cmd);
            // 读取最新的消息对象并打印
            rocksDBTest.getInfo();

            // 创建bucket,8082
            System.setProperty("clientPort","8088");
            RocksDBTest rocksDBTest1 = new RocksDBTest();
            Cmd cmd1 = new Cmd();
            cmd1.setKey("特工刘3");
            cmd1.setValue("你好 rdbms.");
            rocksDBTest1.save(cmd1);
            rocksDBTest1.getInfo();
        }
    }
}
