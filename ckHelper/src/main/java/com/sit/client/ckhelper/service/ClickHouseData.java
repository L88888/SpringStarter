package com.sit.client.ckhelper.service;

import com.sit.client.ckhelper.models.manager.ClickHouseClient;
import com.sit.client.ckhelper.models.manager.http.ClickHouseResponse;
import com.sit.client.ckhelper.models.utils.Tools;
import com.sit.client.config.ClickHouseProperties;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @program: spring-starter
 * @description: ck数据管理，基于http协议
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-04-14 19:20:
 **/
@Data
public class ClickHouseData implements CkClient{

    private static final Logger LOG = LoggerFactory.getLogger(ClickHouseData.class);

    private ClickHouseProperties properties;
//    /**
//     * click house 站点信息IP与port
//     * todo 后期可从配置文件中读取
//     */
//    private String endpoint = "http://172.20.32.150:20212";
//
//    /**
//     * 默认用户名
//     * todo 后期可从配置文件中读取
//     */
//    private String username = "default";
//
//    /**
//     * 密码
//     * todo 后期可从配置文件中读取
//     */
//    private String password = "sailing@123";

    public ClickHouseData() {
    }

    public ClickHouseData(ClickHouseProperties properties) {
        this.properties = properties;
    }

    /**
     * 查询统计数据
     * @param sql 定义查询的sql文
     * @param clazz 定义查询结果的对象结构
     * @param <T> 待返回的数据结果集泛型
     * @return
     */
    public <T> List<T> queryStaticsData(String sql, Class<T> clazz){
        if (Tools.isEmpty(sql) || Objects.isNull(clazz)){
            LOG.debug("未检测到sql文档或结果集对象。sql:>{},resData:>{}", sql, clazz);
            return Collections.emptyList();
        }

        try(ClickHouseClient client = getConnect()){
            CompletableFuture<ClickHouseResponse<T>> resDataTemp =
                    client.get(sql, clazz);
            return resDataTemp.get().data;
        }catch (Exception e){
            LOG.error("查询统计数据异常.异常堆栈为", e.fillInStackTrace());
        }
        return Collections.emptyList();
    }

    /**
     * 查询业务数据
     * @param sql 定义查询的sql文,可支持带条件where查询
     * @param clazz 定义查询结果的对象结构
     * @param <T> 待返回的数据结果集泛型
     * @return
     */
    public <T> List<T> queryData(String sql, Class<T> clazz){
        if (Tools.isEmpty(sql) || Objects.isNull(clazz)){
            LOG.debug("未检测到sql文档或结果集对象。sql:>{},resData:>{}", sql, clazz);
            return Collections.emptyList();
        }

        try(ClickHouseClient client = getConnect()){
            CompletableFuture<ClickHouseResponse<T>> resDataTemp =
                    client.get(sql, clazz);
            return resDataTemp.get().data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 执行一些带计算的业务
     * @param sql 定义查询的sql文,可支持带条件where查询
     * @return
     */
    public String execDataOperation(String sql){
        if (Tools.isEmpty(sql)){
            LOG.debug("未检测到sql文档。sql:>{}", sql);
            return Tools.FAIL;
        }

        try(ClickHouseClient client = getConnect()){
            CompletableFuture<String> resDataTemp =
                    client.post(sql);
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Tools.FAIL;
    }

    /**
     * 新增业务数据
     * @param sql 定义新增的sql文
     * @param rows 定义业务数据行对象
     * @return
     */
    public String insertRecord(String sql, Object[] rows){
        if (Tools.isEmpty(sql) || Objects.isNull(rows)){
            LOG.debug("未检测到sql文档或结果集对象。sql:>{},resData:>{}", sql, rows);
            return Tools.FAIL;
        }

        try(ClickHouseClient client = getConnect()){
            List<Object[]> r1 = new ArrayList<>(1);
            r1.add(rows);

            CompletableFuture<String> resDataTemp =
                    client.post(sql, r1);
            r1 = null;
            rows = null;
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Tools.FAIL;
    }

    /**
     * 新增批量业务数据
     * @param sql 定义批量新增的sql文
     * @param rows 定义批量新增的业务数据集合
     * @return
     */
    public String batchInsertRecord(String sql, List<Object[]> rows){
        if (Tools.isEmpty(sql) || Objects.isNull(rows)){
            LOG.debug("未检测到sql文档或结果集对象。sql:>{},resData:>{}", sql, rows);
            return Tools.FAIL;
        }

        try(ClickHouseClient client = getConnect()){
            CompletableFuture<String> resDataTemp =
                    client.post(sql, rows);
            rows = null;
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Tools.FAIL;
    }

    /**
     * 可通过分区数据值直接删除指定分区的数据（一般较为常用）
     * @param sql 定义分区删除的sql文
     * @return
     */
    @Override
    public String dropPartitionRecord(String sql) {
        if (Tools.isEmpty(sql)){
            LOG.debug("未检测到sql文档或结果集对象。sql:>{},resData:>{}", sql);
            return Tools.FAIL;
        }

        try(ClickHouseClient client = getConnect()){
            CompletableFuture<String> resDataTemp =
                    client.post(sql);
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Tools.FAIL;
    }

    /**
     * 获取ck链接
     * @return
     */
    private ClickHouseClient getConnect(){
        return new ClickHouseClient(properties.getEndpoint(),properties.getUsername(),properties.getPassword());
    }
}
