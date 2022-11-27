package com.tgl.rdbms.core;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import com.tgl.rdbms.ConnPoolConfig.TglPoolConfig;
import com.tgl.rdbms.concurrent.SpringBean;
import com.tgl.rdbms.concurrent.TglRaftThreadHelper;
import com.tgl.rdbms.concurrent.TglRaftThreadPool;
import com.tgl.rdbms.entity.CheckTransInfoDto;
import com.tgl.rdbms.entity.CommodityOrderQueue;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConnectionProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-07-11 16:18:
 **/
@Slf4j
//@Component
public class CommodityOrder {

    /**
     * 初始化数据源对象DataSource
     */
//    @Autowired
    private TglPoolConfig tglPoolConfig;

    /**
     * 初始化PreparedStatement对象
     */
    PreparedStatement preparedStatementNode = null;

    Semaphore semaphore = new Semaphore(2);

    Connection connection = null;
    public CommodityOrder(){
        try {
//            tglPoolConfig = (TglPoolConfig)SpringBean.getBean("tglPoolConfig");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData(){
        try {
            while (true){
                if (semaphore.availablePermits() > 0){
                    semaphore.acquire();
                    processData();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
        }
    }

    private void processData(){
        TglRaftThreadPool.execute(()-> {
            List<CheckTransInfoDto> checkTransInfoDtoList = new ArrayList<>();
            if (CommodityOrderQueue.TEMPDATATO.size() > 0){
                if (CommodityOrderQueue.TEMPDATATO.size() >= 10000) {
                    log.info("read Excel Data is value:>>>>>{}tempIndex data value:{}", CommodityOrderQueue.TEMPDATATO.size(),
                            ";输出线程对象::{}", Thread.currentThread().getName() + Thread.currentThread().getId());
                    try {
                        for (int z = 0;z < 10000;z++){
                            checkTransInfoDtoList.add(CommodityOrderQueue.TEMPDATATO.poll());
                            if (CommodityOrderQueue.TEMPDATATO.size() == 0){
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                        TglRaftThreadHelper.sleep(100);
                }else {
                    for (int z = 0;z < CommodityOrderQueue.TEMPDATATO.size();z++){
                        checkTransInfoDtoList.add(CommodityOrderQueue.TEMPDATATO.poll());
                        if (CommodityOrderQueue.TEMPDATATO.size() == 0){
                            break;
                        }
                    }
                }
                addBatchCommodityOrders(checkTransInfoDtoList);
                TglRaftThreadHelper.sleep(100);
            }else {
                TglRaftThreadHelper.sleep(1000);
            }
            // 重置信号量
            semaphore.release();
        });
    }

    volatile int size = 0;
    public JSONObject addBatchCommodityOrders(List<CheckTransInfoDto> checkTransInfoDtos) {
        log.info("待入库的数据对象为:>{}", checkTransInfoDtos.size());
        JSONObject result = new JSONObject();
        int successCount = 0;
        int failCount = 0;
        DruidPooledConnection connection = null;
        PreparedStatement preparedStatementNode = null;
        try {
            connection = DBConnectionPool.getConnection();
            connection.setAutoCommit(false);
            connection.setReadOnly(false);
            String sql = "INSERT INTO COMMODITYORDER(ID,TRANSORDERNO,TRANSMERCID,SMTMERCID,CRDNO)" +
                    "      VALUES " +
                    "(?,?,?,?,?)";
            preparedStatementNode = connection.prepareStatement(sql);
            for (CheckTransInfoDto data : checkTransInfoDtos) {
                preparedStatementNode.setObject(1, UUID.randomUUID().toString());
                preparedStatementNode.setObject(2, data.getTransOrderNo());
                preparedStatementNode.setObject(3, data.getTransMercId());
                preparedStatementNode.setObject(4, data.getSmtMercId());
                preparedStatementNode.setObject(5, data.getCrdNo());
                preparedStatementNode.addBatch();
            }

            preparedStatementNode.executeBatch();
            connection.commit();
        } catch (Exception e) {
            log.error("创建数据库连接异常，异常信息为:{},,,{}", e.fillInStackTrace(), connection);
            try {
                // 需要把这一批次的数据放入队列中
                connection.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return result;
        }finally {
            if (preparedStatementNode != null) {
                try {
                    preparedStatementNode.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接异常，异常信息为:{}", e.fillInStackTrace());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接异常，异常信息为:{}", e.fillInStackTrace());
                }
            }
        }
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }

    /**
     * 批量新增业务数据的操作
     * @param checkTransInfoDtos Excel数据
     * @return JSONObject
     */
    public JSONObject addBatchCommodityOrder(List<CheckTransInfoDto> checkTransInfoDtos) {
        JSONObject result = new JSONObject();
        int successCount = 0;
        int failCount = 0;
        Connection connection = null;
        // 存储车辆数据集合
        CheckTransInfoDto data = null;
        int size = checkTransInfoDtos.size();
        try {
            connection = tglPoolConfig.dataSource().getConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO COMMODITYORDER(ID,TRANSORDERNO,TRANSMERCID,SMTMERCID,CRDNO)" +
                    "      VALUES " +
                    "(?,?,?,?,?)";
            preparedStatementNode = connection.prepareStatement(sql);
            // 遍历list集合中的车辆业务数据对象
            int i = 0;
            for (i = 0; i < size; i++) {
                data = checkTransInfoDtos.get(i);
                if (data == null) {
                    failCount++;
                    continue;
                }
                // 对号牌号码进行判断为空直接不添加使用日志输出
                successCount++;
                Object[] parameterr = {
                        UUID.randomUUID().toString(),
                        data.getTransOrderNo(),
                        data.getTransMercId(),
                        data.getSmtMercId(),
                        data.getCrdNo()
                };
                setOperationData(preparedStatementNode, sql, parameterr, size - 1, i);
            }
            connection.commit();
        } catch (Exception e) {
            log.error("创建数据库连接异常，异常信息为:{}", e.fillInStackTrace());
            try {
                // 需要把这一批次的数据放入队列中
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return result;
        } finally {
            if (preparedStatementNode != null) {
                try {
                    preparedStatementNode.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接异常，异常信息为:{}", e.fillInStackTrace());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接异常，异常信息为:{}", e.fillInStackTrace());
                }
            }
        }
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }

    /**
     * 开始执行预处理的sql语句
     * @param preparedStatement
     * @param sql
     * @param parameter
     * @param dataSize
     * @param index
     * @throws SQLException
     */
    protected void setOperationData(PreparedStatement preparedStatement, String sql, Object[] parameter,
                                    int dataSize, int index) throws SQLException {
        if (parameter != null && parameter.length > 0) {
            for (int i = 0; i < parameter.length; i++) {
                preparedStatement.setObject(i + 1, parameter[i]);
            }
        }
        if (false) {
            log.info("sql语句为：{},对应参数为：{}", sql,
                    parameter != null && parameter.length > 0 ? Arrays.toString(parameter) : null);
        }
        preparedStatement.addBatch();
        if (index != 0 && index % 5000 == 0) {
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            log.info("--------------------------{}>>>{}", dataSize, index);
        }
        if (dataSize == index) {
            log.info("++++++++++++++++++++++++++{}>>>{}", dataSize, index);
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
            log.info("==========================={}>>>{}", dataSize, index);
        }
    }
}
