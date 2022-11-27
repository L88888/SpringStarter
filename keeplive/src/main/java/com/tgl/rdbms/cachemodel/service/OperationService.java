package com.tgl.rdbms.cachemodel.service;

import com.tgl.rdbms.cachemodel.entity.Command;
import com.tgl.rdbms.cachemodel.entity.LogEntry;
import com.tgl.rdbms.cachemodel.entity.ThirdPartyLog;
import com.tgl.rdbms.cachemodel.rocksdb.DefaultLogEntryImpl;
import org.springframework.stereotype.Service;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-02 11:12:
 **/
@Service
public class OperationService {

    /**
     * 存储日志数据至缓存rocksdb中
     * @param thirdPartyLog
     */
    public void logDataWrite(ThirdPartyLog thirdPartyLog){
        Command command = new Command(thirdPartyLog.getId(), thirdPartyLog.getName());
        LogEntry logEntry = new LogEntry();
        logEntry.setIndex(1L);
        logEntry.setTerm(2);
        logEntry.setCommand(command);
        DefaultLogEntryImpl.getInstance().write(logEntry);
    }

    public void logDataWrite(ThirdPartyLog thirdPartyLog,int mock10w){
        for (int i = 0;i < 100000;i++){
            Command command = new Command(thirdPartyLog.getId() + i,
                    thirdPartyLog.getName() + i);
            LogEntry logEntry = new LogEntry();
            logEntry.setIndex(1L);
            logEntry.setTerm(i);
            logEntry.setCommand(command);
            DefaultLogEntryImpl.getInstance().write(logEntry);
        }
    }

    /**
     * 获取日志数据至缓存rocksdb中
     * @param logId
     */
    public LogEntry getLogData(String logId){
        return DefaultLogEntryImpl.getInstance().read(logId);
    }
}
