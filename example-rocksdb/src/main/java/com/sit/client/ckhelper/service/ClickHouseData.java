package com.sit.client.ckhelper.service;

import com.sit.client.ckhelper.models.ClickHouseClient;
import com.sit.client.ckhelper.models.manager.http.ClickHouseResponse;

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
public class ClickHouseData {

    /**
     * click house 站点信息IP与port
     */
    private String endpoint = "http://182.168.80.20:20212";

    /**
     * 默认用户名
     */
    private String username = "default";

    /**
     * 密码
     */
    private String password = "sailing@123";

    /**
     * 查询统计数据
     * @param sql 定义查询的sql文
     * @param clazz 定义查询结果的对象结构
     * @param <T> 待返回的数据结果集泛型
     * @return
     */
    public <T> List<T> queryStaticsData(String sql, Class<T> clazz){
        try(ClickHouseClient client =
                new ClickHouseClient(endpoint,username,password)){
            CompletableFuture<ClickHouseResponse<T>> resDataTemp =
                    client.get(sql, clazz);
            return resDataTemp.get().data;
        }catch (Exception e){
            e.printStackTrace();
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
        try(ClickHouseClient client =
                    new ClickHouseClient(endpoint,username,password)){
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
     * @param clazz 定义查询结果的对象结构
     * @param <T>
     * @return
     */
    public <T> List<T> execDataOperation(String sql, Class<T> clazz){
        try(ClickHouseClient client =
                    new ClickHouseClient(endpoint,username,password)){
            CompletableFuture<ClickHouseResponse<T>> resDataTemp =
                    client.post(sql, clazz);
            return resDataTemp.get().data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 新增业务数据
     * @param sql 定义新增的sql文
     * @param rows 定义业务数据行对象
     * @return
     */
    public String insertRecord(String sql, Object[] rows){
        if (Objects.isNull(rows)){
            return "未检测到待新增的行数据记录.";
        }

        try(ClickHouseClient client =
                    new ClickHouseClient(endpoint,username,password)){
            List<Object[]> r1 = new ArrayList<>(1);
            r1.add(rows);

            CompletableFuture<String> resDataTemp =
                    client.post(sql, r1);
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 新增批量业务数据
     * @param sql 定义批量新增的sql文
     * @param rows 定义批量新增的业务数据集合
     * @return
     */
    public String batchInsertRecord(String sql, List<Object[]> rows){
        if (Objects.isNull(rows)){
            return "未检测到待新增的批量业务数据记录.";
        }

        try(ClickHouseClient client =
                    new ClickHouseClient(endpoint,username,password)){
            CompletableFuture<String> resDataTemp =
                    client.post(sql, rows);
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 可通过分区数据值直接删除指定分区的数据（一般较为常用）
     * @param sql
     * @return
     */
    public String dropPartitionRecord(String sql){
        try(ClickHouseClient client =
                    new ClickHouseClient(endpoint,username,password)){
            CompletableFuture<String> resDataTemp =
                    client.post(sql);
            return resDataTemp.get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
}