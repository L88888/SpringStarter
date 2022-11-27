package com.rocksdb;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

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
        System.out.println("【最新的下标值LastIndex】>>>" + Long.valueOf(new String(lastIndex)));
        return Long.valueOf(new String(lastIndex));
    }

    /**
     * 只存储带索引的消息对象
     * @param cmd
     */
    private void save(Cmd cmd){
        try {
            cmd.setIndex(getLastIndex() + 1);
            machineDb.put(cmd.getIndex().toString().getBytes(), JSON.toJSONBytes(cmd));
            System.out.println("【保存业务数据成功】>>>" + cmd);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }finally {
            updateLastIndex(cmd.getIndex());
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
            resCmd = JSON.parseObject(resData, Cmd.class);
            System.out.println("【获取到的消息对象值】>>>" + resCmd);
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

    public static void main(String[] agr){
        // 创建bucket,8081
        System.setProperty("clientPort","8085");
        RocksDBTest rocksDBTest = new RocksDBTest();
        Cmd cmd = new Cmd();
        cmd.setKey("gtl3");
        cmd.setValue("你好 rdbms.");
        rocksDBTest.save(cmd);
        rocksDBTest.write(cmd);
        // 读取最新的消息对象并打印
        rocksDBTest.getInfo();

        // 创建bucket,8082
        System.setProperty("clientPort","8086");
        RocksDBTest rocksDBTest1 = new RocksDBTest();
        Cmd cmd1 = new Cmd();
        cmd1.setKey("特工刘3");
        cmd1.setValue("你好 rdbms.");
        rocksDBTest1.save(cmd1);
        rocksDBTest1.getInfo();

    }
}
