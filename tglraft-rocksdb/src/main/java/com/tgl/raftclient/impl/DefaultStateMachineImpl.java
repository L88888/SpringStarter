package com.tgl.raftclient.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.remoting.util.StringUtils;
import com.tgl.raftclient.core.StateMachine;
import com.tgl.raftclient.entity.Command;
import com.tgl.raftclient.entity.LogEntry;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: spring-starter
 * @description: 采用rocksdb 实现底层key/value数据存储（增删改查）
 * @author: LIULEI-TGL
 * @create: 2021-05-18 21:42:
 **/
@Slf4j
public class DefaultStateMachineImpl implements StateMachine {

    /**
     * 添加日志数据值状态机时枷锁
     */
    ReentrantLock stateLock = new ReentrantLock();

    // System.setProperty("rocksdbPath","F:/rdbms-test/20211/logModule");日志数据默认目录
    // System.setProperty("rocksdbPath","F:/rdbms-test/20211/stateMachine");状态机默认目录
    /** db存储目录 */
    private static String dbDir;
    /** 日志存储目录 */
    private static String stateDir;

    private static RocksDB rocksDB;

    /**
     * 初始化状态机对象存储目录
     */
    static {
        if (dbDir == null){
            // todo 盘符可以自定义指定
            dbDir = "F:/rdbms-test/" + System.getProperty("clientPort");
        }

        if (stateDir == null){
            stateDir = dbDir.concat("/stateMachine");
        }
        RocksDB.loadLibrary();
    }

    private DefaultStateMachineImpl(){
        synchronized (this){
            boolean sucess = false;

            // 创建文件夹
            File logFile = new File(stateDir);
            if (!logFile.exists()){
                logFile.mkdirs();
                sucess = true;
            }

            if (sucess){
                log.info("创建rockdb 状态机db文件目录成功.");
            }

            try {
                Options options = new Options();
                options.setCreateIfMissing(true);
                rocksDB = RocksDB.open(options, stateDir);
            } catch (RocksDBException e) {
                e.printStackTrace();
                log.info("创建rockdb 文件异常，异常信息为:>{}", e.getStackTrace());
            }
        }
    }

    public static DefaultStateMachineImpl getInstance(){
        return DefaultStateMachineLazyHolder.getInstance();
    }

    private static final class DefaultStateMachineLazyHolder{
        private volatile static DefaultStateMachineImpl INSTANCESTATEMACHINE;

        public static DefaultStateMachineImpl getInstance(){
            if (INSTANCESTATEMACHINE == null){
                synchronized (DefaultStateMachineLazyHolder.class){
                    if (INSTANCESTATEMACHINE == null){
                        INSTANCESTATEMACHINE = new DefaultStateMachineImpl();
                    }
                }
            }
            return INSTANCESTATEMACHINE;
        }
    }

    /**
     * 添加日志数据至状态机存储
     * @param logEntry 日志条目对象
     */
    @Override
    public void apply(LogEntry logEntry) {
        try {
            stateLock.tryLock(3000, TimeUnit.MILLISECONDS);
            if (logEntry.getCommand() == null){
                throw new ReflectiveOperationException("写入的命令对象为null,无法添加" + logEntry);
            }

            rocksDB.put(logEntry.getCommand().getKey().getBytes(), JSON.toJSONBytes(logEntry));
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("添加日志数据至状态机存储异常，异常信息为:>{}", e.fillInStackTrace());
        } finally {
            stateLock.unlock();
        }
    }

    @Override
    public LogEntry get(String key) {
        LogEntry resVal = new LogEntry();
        if (StringUtils.isBlank(key)){
            log.warn("入参不能为空key:>{}", key);
            return resVal;
        }

        try {
            byte[] res = rocksDB.get(key.getBytes());
            if (res != null && res.length > 0){
                return JSON.parseObject(res, LogEntry.class);
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("状态机数据检索异常，异常信息为:>{}", e.getStackTrace());
        }
        return resVal;
    }

    @Override
    public String getString(String key) {
        String resVal = "";
        if (StringUtils.isBlank(key)){
            log.warn("入参不能为空key:>{}", key);
            return resVal;
        }

        try {
            byte[] resVal1 = rocksDB.get(key.getBytes());
            if (resVal1 != null && resVal1.length > 0){
                return JSON.parseObject(resVal1, LogEntry.class).toString();
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("状态机数据检索异常，异常信息为:>{}", e.getStackTrace());
        }
        return resVal;
    }

    @Override
    public void setString(String key, String value) {
        if (StringUtils.isBlank(key) ||  StringUtils.isBlank(value)){
            log.warn("入参不能为空key:>{},value:>{}", key, value);
            return;
        }

        try {
            rocksDB.put(key.getBytes(), value.getBytes());
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("状态机写入数据异常，异常信息为:>{}", e.getStackTrace());
        }
    }

    @Override
    public void delString(String... keys) {
        if (keys == null){
            log.info("无效key对象:>{}",keys);
            return;
        }

        try {
            for (String valKey : keys){
                rocksDB.delete(valKey.getBytes());
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("删除key对象:>{}异常，异常信息为:>{}", keys, e.getStackTrace());
        }
    }

    @Override
    public String getCommandData(String key) {
        String resVal = "";
        if (StringUtils.isBlank(key)){
            log.warn("入参不能为空key:>{}", key);
            return resVal;
        }

        try {
            byte[] resVal1 = rocksDB.get(key.getBytes());
            if (resVal1 != null && resVal1.length > 0){
                LogEntry logEntry = JSON.parseObject(resVal1, LogEntry.class);
                if (logEntry != null){
                    return logEntry.getCommand().toString();
                }
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("状态机数据检索异常，异常信息为:>{}", e.getStackTrace());
        }
        return resVal;
    }

    @Override
    public Command getCommandInfo(String key) {
        Command resVal = Command.newBuilder().build();
        if (StringUtils.isBlank(key)){
            log.warn("入参不能为空key:>{}", key);
            return resVal;
        }

        try {
            byte[] res = rocksDB.get(key.getBytes());
            if (res != null && res.length > 0){
                LogEntry logEntry = JSON.parseObject(res, LogEntry.class);
                if (logEntry != null){
                    return logEntry.getCommand();
                }
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("状态机数据检索异常，异常信息为:>{}", e.getStackTrace());
        }
        return resVal;
    }
}
