package com.tgl.rdbms.core;

import com.alibaba.fastjson.JSONObject;
import com.alipay.remoting.util.StringUtils;
import com.tgl.rdbms.ConnPoolConfig.TglPoolConfig;
import com.tgl.rdbms.entity.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-27 17:46:
 **/
@Slf4j
@Component
public class DbHelp {
    /**
     * 初始化数据源对象DataSource
     */
    @Autowired
    private TglPoolConfig tglPoolConfig;

    /**
     * 初始化PreparedStatement对象
     */
    PreparedStatement preparedStatementNode = null;

    /**
     * 批量新增业务数据的操作
     * @param excelData Excel数据
     * @return JSONObject
     */
    public JSONObject addBatchVehiclesblacklibDetailedInfo(List<Vehicle> excelData) {
        JSONObject result = new JSONObject();
        int successCount = 0;
        int failCount = 0;
        Connection connection = null;
        // 存储车辆数据集合
        Vehicle data = null;
        String vehicleBrandNum = "";
        int size = excelData.size();
        try {
            connection = tglPoolConfig.dataSource().getConnection();
            connection.setAutoCommit(false);
            String sql = "insert into VEHICLE(" +
                    "        id,plate_type,plate_no,speed,appear_time," +
                    "        mark_time,device_id,vehicle_image,scene_image,vehicle_color," +
                    "        area_code,line_no,pass_time,plate_color,plate_describe," +
                    "        disappear_time,vehicle_class" +
                    "      )" +
                    "      values " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatementNode = connection.prepareStatement(sql);
            // 遍历list集合中的车辆业务数据对象
            int i = 0;
            for (i = 0; i < size; i++) {
                data = excelData.get(i);
                if (data == null) {
                    failCount++;
                    continue;
                }
                // 对号牌号码进行判断为空直接不添加使用日志输出
                vehicleBrandNum = String.valueOf(data.getPlate_no());
                if (StringUtils.isBlank(vehicleBrandNum) || "null".equals(vehicleBrandNum)) {
                    log.info("导入数据中的号牌号码不存在或者已存在同名的号牌号码，不能导入:{}", vehicleBrandNum);
                    failCount++;
                    continue;
                }
                successCount++;
                Object[] parameterr = {
                        data.getId(),
                        data.getPlate_type(),
                        data.getPlate_no(),
                        data.getSpeed(),
                        data.getAppear_time(),
                        data.getMark_time(),
                        data.getDevice_id(),
                        data.getVehicle_image(),
                        data.getScene_image(),
                        data.getVehicle_color(),
                        data.getArea_code(),
                        data.getLine_no(),
                        data.getPass_time(),
                        data.getPlate_color(),
                        data.getPlate_describe(),
                        data.getDisappear_time(),
                        data.getVehicle_class()
                };
                //log.info("获取的车辆导入数据为:{}", resultData_.get("vehicleBrandNum"));
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
        if (index != 0 && index % 10000 == 0) {
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
        }
        if (dataSize == index) {
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
        }
    }
}
