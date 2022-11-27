package com.tgl.rdbms.cachemodel.rocksdb;

import com.alibaba.fastjson.JSON;
import com.tgl.rdbms.cachemodel.RaftUtils;
import com.tgl.rdbms.cachemodel.entity.LogEntry;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: spring-starter
 * @description: 日志模块的方法实现
 * @author: LIULEI-TGL
 * @create: 2021-05-20 15:52:
 **/
@Slf4j
public class DefaultLogEntryImpl implements LogModule {

    /**
     * 日志模块的锁对象
     */
    ReentrantLock lockLog = new ReentrantLock();

    /** db存储目录 */
    private static String dbDir;
    /** 日志存储目录 */
    private static String logsDir;

    private static RocksDB rocksDB;

//    final Statistics stats = new Statistics();

    /**
     * 初始化日志存储目录
     */
    static {
        if (dbDir == null){
            dbDir = "F:/rdbms-test/" + "log";
        }

        if (logsDir == null){
            logsDir = dbDir.concat("/logModule");
        }
        RocksDB.loadLibrary();
    }

    private DefaultLogEntryImpl(){
        synchronized (this){
            boolean sucess = false;

            // 创建文件夹
            File logFile = new File(logsDir);
            if (!logFile.exists()){
                logFile.mkdirs();
                sucess = true;
            }

            if (sucess){
                log.info("创建rockdb 日志文件目录成功.");
            }

            try {
                Options options = new Options();
                options.setCreateIfMissing(true);
////                        .setStatistics(stats)
////                        .setWriteBufferSize(8 * SizeUnit.KB)
////                        .setMaxWriteBufferNumber(3)
////                        .setMaxBackgroundJobs(10)
//                        .setCompressionType(CompressionType.SNAPPY_COMPRESSION)
//                        .setCompactionStyle(CompactionStyle.UNIVERSAL);
//                rocksDB = RocksDB.openReadOnly(logsDir);
                rocksDB = RocksDB.open(options,logsDir);
            } catch (RocksDBException e) {
                e.printStackTrace();
                log.info("创建rockdb 文件异常，异常信息为:>{}", e.getStackTrace());
            }
        }
    }

    /**
     * 懒加载对象
     */
    private static class DefaultLogEntryLazyHolder{
        // TODO 后期需要优化
        private static volatile DefaultLogEntryImpl defaultLogEntry;

        public static final DefaultLogEntryImpl getInstance(){
            if (defaultLogEntry == null){
                synchronized (DefaultLogEntryLazyHolder.class){
                    if (defaultLogEntry == null){
                        defaultLogEntry = new DefaultLogEntryImpl();
                    }
                }
            }
            return defaultLogEntry;
        }
    }

    /**
     * 初始化
     * @return
     */
    public static DefaultLogEntryImpl getInstance(){
        return DefaultLogEntryLazyHolder.getInstance();
    }

    /**
     * 批量写入时需要枷锁，否则多线程模式下会出现死锁问题
     * @param logEntry
     */
    @Override
    public void write(LogEntry logEntry) {
        boolean success = false;
        try {
            lockLog.tryLock(2000, TimeUnit.MILLISECONDS);
            // todo 需要做消息数据值判断，如果为空不能写入
            // 获取最大下标值
            rocksDB.put(logEntry.getCommand().getKey().getBytes(),
                    JSON.toJSONBytes(logEntry));
            success = true;
            log.warn("写入rockdb成功，写入的数据对象为:>{}", logEntry);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("写入rockdb数据异常，异常信息为:>{}", e.fillInStackTrace());
        } finally {
            // 更新last_index_key最后的下标值
            if (success){
                upDataLogEntry(logEntry.getIndex());
            }
            lockLog.unlock();
        }
    }

    /**
     * 按照下标获取日志对象
     * @param key
     * @return
     */
    @Override
    public LogEntry read(String key) {
        final ReadOptions readOptions = new ReadOptions().setFillCache(false);
        try {
            byte[] resaultData = rocksDB.get(readOptions,key.getBytes());
            if (resaultData != null){
                return JSON.parseObject(resaultData, LogEntry.class);
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("获取指定下标的日志对象异常，异常信息为:>{}", e.getStackTrace());
        }
        return new LogEntry();
    }

    /**
     * 删除时需要枷锁，防止多线程死锁问题.
     * 删除指定下标处之后的所有索引对象。
     * @param startIndex 待删除的日志数据开始索引下标
     */
    @Override
    public void removeOnStartIndex(Long startIndex) {
        if (startIndex == -1){
            log.info("起始索引值为-1,不需要删除日志对象数据.");
            return;
        }

        boolean success = false;
        int count = 0;
        try {
            lockLog.tryLock(3000, TimeUnit.MILLISECONDS);
            for (long i = startIndex;i <= getLastIndex();i++){
                rocksDB.delete(String.valueOf(i).getBytes());
                count++;
            }
            success = true;
            System.out.println("总删除日志数据:>{}"+count+",开始下标:>{}"+startIndex+",最后的下标:>{}" + getLastIndex());
            log.warn("总删除日志数据:>{},开始下标:>{},最后的下标:>{}", count, startIndex, getLastIndex());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 更新最后的节点下标值
            if (success){
                System.out.println("批量删除后的下标值:>>" + (getLastIndex() - count));
                upDataLogEntry(getLastIndex() - count);
            }
            lockLog.unlock();
        }
    }

    /**
     * 获取最后一条日志对象
     * @return
     */
    @Override
    public LogEntry getLast() {
        try {
            byte[] val = rocksDB.get(getLastIndex().toString().getBytes());
            if (val != null){
                return JSON.parseObject(val, LogEntry.class);
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("获取最后一条日志数据对象异常，异常信息为:>{}", e.getStackTrace());
        }
        return new LogEntry();
    }

    /**
     * 获取最后一个下标值
     * @return
     */
    @Override
    public Long getLastIndex() {
        byte[] val = RaftUtils.getInstance().getInvalidIndexKey();
        try {
            val = rocksDB.get(RaftUtils.getInstance().getLastIndexKey());
            if (val == null){
                val = RaftUtils.getInstance().getInvalidIndexKey();
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
            log.debug("获取最后一个下标数据值异常，异常信息为:>{}", e.getStackTrace());
        }
        return Long.valueOf(new String(val));
    }

    /**
     * 用于给批量新增后一次性更新lastindex的最后值数据
     * @param lastIndex
     */
    private void upDataLogEntry(Long lastIndex){
//        try {
//            rocksDB.put(RaftUtils.getInstance().getLastIndexKey(), lastIndex.toString().getBytes());
//        } catch (RocksDBException e) {
//            e.printStackTrace();
//        }
    }
}
