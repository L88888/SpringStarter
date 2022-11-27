package com.tgl.rdbms.core;

import com.alibaba.fastjson.JSONObject;
import com.alipay.remoting.util.StringUtils;
import com.tgl.rdbms.ConnPoolConfig.TglPoolConfig;
import com.tgl.rdbms.fileutils.RaftUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-27 17:27:
 **/
@Repository
@Slf4j
public class VehiclesBlacklibDetailedDao {

    /**
     * 初始化数据源对象DataSource
     */
//    @Autowired
    private TglPoolConfig druidConfig;

    /**
     * 初始化PreparedStatement对象
     */
    PreparedStatement preparedStatementNode = null;

    /**
     * 批量新增业务数据的操作
     * @param excelData Excel数据
     * @return JSONObject
     */
    public JSONObject addBatchVehiclesblacklibDetailedInfo(List<Map<Integer,Object>> excelData, String tarDir,
                                                           String vehicleId, String userName, String areaname) {
        JSONObject result = new JSONObject();
        int successCount = 0;
        int failCount = 0;
        Connection connection = null;
        // 存储车辆数据集合
        Map data = null;
        String vehicleBrandNum = "";
        int size = excelData.size();
        try {
            connection = druidConfig.dataSource().getConnection();
            String sql = "INSERT INTO VEHICLES_BLACKLIB_DETAILED_T (ID, VEHICLE_ID, VEHICLE_BRAND_NUM, VEHICLE_PLATE_COLOR,VEHICLE_IMAGE_URL,\n" +
                    "            VEHICLE_BRAND,VEHICLE_BODY_COLOR,VEHICLE_OWNER,VEHICLE_OWNER_PHONE,\n" +
                    "            VEHICLE_OWNER_CARD,CREATE_TIME,CREATE_PERSON,CREATE_COMPANY)" +
                    "VALUES " +
                    "(?,?,?, ?,?,?, ?,?,?, ?,to_timestamp(?,'yyyy-mm-dd hh24:mi:ss') ::timestamp without time zone,?, ?)";
            preparedStatementNode = connection.prepareStatement(sql);
            // 遍历list集合中的车辆业务数据对象
            int i = 0;
            for (i = 0; i < size; i++) {
                data = excelData.get(i);
                if (data == null || data.isEmpty()) {
                    failCount++;
                    continue;
                }
                // 对号牌号码进行判断为空直接不添加使用日志输出
                vehicleBrandNum = String.valueOf(data.get(0));
                if (StringUtils.isBlank(vehicleBrandNum) || "null".equals(vehicleBrandNum)) {
                    log.info("导入数据中的号牌号码不存在或者已存在同名的号牌号码，不能导入:{}", vehicleBrandNum);
                    failCount++;
                    continue;
                }
                successCount++;
                // {'id':'车辆id','vehicleId':'车辆库id','vehicleBrandNum':'号牌号码','vehiclePlateColor':'号牌颜色','vehicleImageUrl':'车辆图片地址',
                // 'vehicleBrand':'车辆类型','vehicleBodyColor':'车身颜色','vehicleOwner':'车辆所有人','vehicleOwnerPhone':'联系电话',
                // 'vehicleOwnerCard':'身份证','createPerson':'创建人','createCompany':'创建单位'}
                // {"号牌号码", "号牌颜色", "车辆品牌", "车身颜色", "车辆所属人", "所属人证件号", "车辆图片"};
                String vehicleImageUrl = null == data.get(7) || StringUtils.isEmpty(String.valueOf(data.get(7))) ?
                        null:""+ tarDir.replaceFirst("/home","") + File.separator + data.get(7);
                Object[] parameterr = {
                        "---" + i,
                        vehicleId,
                        getObjStr(vehicleBrandNum.replaceAll("\\t","")),
                        data.get(1),
                        vehicleImageUrl,
                        data.get(2),
                        data.get(3),
                        data.get(4),
                        data.get(5),
                        data.get(6),
                        RaftUtils.getCurrentDate(),
                        userName,
                        areaname};
                //log.info("获取的车辆导入数据为:{}", resultData_.get("vehicleBrandNum"));
                setOperationData(preparedStatementNode, sql, parameterr, size - 1, i);
            }
        } catch (Exception e) {
            log.error("创建数据库连接异常，异常信息为:{}", e.fillInStackTrace());
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
        if (true) {
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

    /**
     * 去除字符串空格
     * @param objData
     * @return
     */
    private Object  getObjStr(Object objData){
        if (null == objData || "".equals(objData)) {
            return objData;
        }
        String data=String.valueOf(objData).replaceAll(" ","");
        return data;
    }
}
