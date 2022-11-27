package com.sailing.tgl.test;

import com.tgl.raftclient.core.LogModule;
import com.tgl.raftclient.core.StateMachine;
import com.tgl.raftclient.entity.Command;
import com.tgl.raftclient.entity.LogEntry;
import com.tgl.raftclient.impl.DefaultLogEntryImpl;
import com.tgl.raftclient.impl.DefaultStateMachineImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Objects;

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
        System.setProperty("clientPort","2021-08-17");
    }

    /**
     * 测试状态机管理(key/value)
     */
    @Test
    public void vehicleRetention(){
        StateMachine stateMachine = DefaultStateMachineImpl.getInstance();
        // 号牌号码_号牌颜色(枚举)
        String bussKey = "陕A99999_05";
        Command command = Command.newBuilder()
                .key(bussKey)
                .value("陕A99999 过车基本信息第二次过车。抓拍时间:2021-08-17 19:58:20")
                .build();

        // 封装日志条目
        LogEntry logEntry = LogEntry.newBuilder()
                .command(command)
                .build();
        // 刷新前校验状态机里头的key存在否?
        // todo 使用状态机来判断是否需要给增加日志条目，如果状态机中有车辆保有量数据。那么日志条目与状态机就不需要再次增加该记录
        LogEntry isexistLog = stateMachine.get(bussKey);
        if (!Objects.isNull(isexistLog.getCommand())){
            log.warn("号牌号码已存在保有量缓存中，无需重复存储.{}", isexistLog);
            // 做更新操作
            stateMachine.apply(logEntry);
            return;
        }

        // 过车信息刷新到日志条目中
        LogModule logModule = DefaultLogEntryImpl.getInstance();
        logModule.write(logEntry);
        // 过车信息刷新到状态机(保有量中)
        // todo 状态机必须与日志条目一起存储执行
        stateMachine.apply(logEntry);

        logEntry = stateMachine.get(bussKey);
        log.info(">>>{}" +  logEntry);

        String bussData = stateMachine.getString(bussKey);
        log.info(">>>{}" + bussData);

        String resData = stateMachine.getCommandData(bussKey);
        log.info(">>>{}" + resData);

        Command commandData = stateMachine.getCommandInfo(bussKey);
        log.info(">>>{}" + commandData);
    }

    /**
     * 测试日志条目logentry
     */
    @Test
    public void getVehicleRetention(){
        LogModule logModule = DefaultLogEntryImpl.getInstance();
        StateMachine stateMachine = DefaultStateMachineImpl.getInstance();

        log.info(">>>{}",logModule.getLastIndex());
        // 遍历日志条目数据,挨个获取日志中的车辆保有量数据
        LogEntry logEntry = null;
        for (long i = 0 ;i<=logModule.getLastIndex();i++){
            logEntry = logModule.read(i);
            if (!Objects.isNull(logEntry.getCommand())){
                logEntry = stateMachine.get(logEntry.getCommand().getKey());
                log.info("车辆保有量数据:>{}", logEntry);
            }
        }
    }
}
