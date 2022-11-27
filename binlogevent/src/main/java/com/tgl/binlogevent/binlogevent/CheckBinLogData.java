package com.tgl.binlogevent.binlogevent;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

/**
 * @program: spring-starter
 * @description: 用于检测mysql中binlog的变化,需要首先开启binlog机制
 *              1、开启binlog（目录为:/etc/mysql/mysql.conf.d/mysql.cnf）
                   log_bin=mysql-bin
                   binlog-format=ROW
                   server-id=1
                 2、查询binlog是否开启
                    show variables like 'log_bin';
 * @author: LIULEI-TGL
 * @create: 2021-07-27 18:38:
 **/
@Slf4j
public class CheckBinLogData {

    private CheckBinLogData(){}
    private volatile static CheckBinLogData CHECKBINLOGDATA;

    public static CheckBinLogData getInstance(){
        if (CHECKBINLOGDATA == null){
            synchronized (CheckBinLogData.class){
                if (CHECKBINLOGDATA == null){
                    CHECKBINLOGDATA = new CheckBinLogData();
                }
            }
        }
        return CHECKBINLOGDATA;
    }


    /**
     * 启动程序后开始一直监听mysql的binlog日志输出
     * 目前主要监听：数据库、表变化情况
     * 1、监听新增表数据
     * 2、监听修改表数据
     * 3、监听删除表数据
     */
    @Async
    public void eventListeners(){
        try {
            // 通过mysql事件监听的机制来处理binlog变化"127.0.0.1", 3306, "hydra", "123456");
            BinaryLogClient binaryLogClient
                    = new BinaryLogClient("182.168.80.20", 3306, "root", "123456");

            binaryLogClient.registerEventListener((event) -> {
                EventData eventData = event.getData();
                if (eventData instanceof TableMapEventData){
                    TableMapEventData tableMapEventData = (TableMapEventData) eventData;
                    log.info("数据表名称:{}, 数据表id:{}, 数据库名称:{}", tableMapEventData.getTable(), tableMapEventData.getTableId(),
                            tableMapEventData.getDatabase());

                    // todo 需要通过缓存将数据表名称与id进行映射绑定
                }

                if (eventData instanceof UpdateRowsEventData){
                    UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) eventData;
                    log.info("数据表id:{}, 被修改的业务数据:{}", updateRowsEventData.getTableId(), updateRowsEventData.getRows());
                    // todo 通过数据表id获取具体是哪张表的业务数据
                }else if(eventData instanceof WriteRowsEventData){
                    WriteRowsEventData writerowseventdata = (WriteRowsEventData) eventData;
                    log.info("数据表id:{}, 新增的业务数据:{}", writerowseventdata.getTableId(), writerowseventdata.getRows());
                    // todo 通过数据表id获取具体是哪张表的业务数据
                }else if(eventData instanceof DeleteRowsEventData){
                    DeleteRowsEventData deleteRowsEventData = (DeleteRowsEventData) eventData;
                    log.info("数据表id:{}, 被修改的业务数据:{}", deleteRowsEventData.getTableId(), deleteRowsEventData.getRows());
                    // todo 通过数据表id获取具体是哪张表的业务数据
                }
            });

            binaryLogClient.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}