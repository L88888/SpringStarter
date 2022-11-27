package com.sailing.tgl.test;

import com.alipay.remoting.util.StringUtils;
import com.tgl.raft.core.StateMachine;
import com.tgl.raft.entity.Command;
import com.tgl.raft.entity.LogEntry;
import com.tgl.raft.impl.DefaultStateMachineImpl;
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
     * 测试状态机管理(key/value)
     */
    @Test
    public void testStsteMachine(){
        System.setProperty("clientPort","1314");
        StateMachine stateMachine = DefaultStateMachineImpl.getInstance();
        String bussKey = "test00909";
        Command command = Command.newBuilder()
                .key(bussKey)
                .value("你好，世界！我可以是任意值.")
                .build();

        LogEntry logEntry = LogEntry.newBuilder()
                .term(12)
                .index(22L)
                .command(command)
                .build();
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
}
