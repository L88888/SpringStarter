package com.sit.client.device.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.BiFunction;

@Service
@Slf4j
public class BatchUtils {

    /**
     * 每次处理2000条
     */
    private static final int BATCH_SIZE = 5000;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 批量处理修改或者插入
     * batchUtils.batchUpdateOrInsert(数据集合, item -> mapper实例对象.insert方法(item));
     *
     * @param data 需要被处理的数据
     * @param mapperClass Mybatis的Mapper类
     * @param function 自定义处理逻辑
     * @return int 影响的总行数
     */
    public  <T,U,R> int batchUpdateOrInsert(List<T> data, Class<U> mapperClass, BiFunction<T,U,R> function) {
        int i = 1;
        SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            U mapper = batchSqlSession.getMapper(mapperClass);
            int size = data.size();
            for (T element : data) {
                function.apply(element,mapper);
                if ((i % BATCH_SIZE == 0) || i == size) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            // 非事务环境下强制commit，事务情况下该commit相当于无效
            batchSqlSession.commit(!TransactionSynchronizationManager.isSynchronizationActive());
        } catch (Exception e) {
            batchSqlSession.rollback();
             log.info("{}", e.fillInStackTrace());
        } finally {
            batchSqlSession.close();
        }
        return i - 1;
    }

    /**
     * 批量处理修改或者插入
     * batchUtils.batchUpdateOrInsert(数据集合, item -> mapper实例对象.insert方法(item));
     *
     * @param data 需要被处理的数据
     * @param mapperClass Mybatis的Mapper类
     * @param function 自定义处理逻辑
     * @return int 影响的总行数
     */
    public  <T,U,R> void batchUpdateOrInserts(List<T> data, Class<U> mapperClass, BiFunction<T,U,R> function) {
        int i = 1;
        SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            U mapper = batchSqlSession.getMapper(mapperClass);
            int size = data.size();
            for (T element : data) {
                function.apply(element,mapper);
                if ((i % BATCH_SIZE == 0) || i == size) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            // 非事务环境下强制commit，事务情况下该commit相当于无效
            batchSqlSession.commit(!TransactionSynchronizationManager.isSynchronizationActive());
        } catch (Exception e) {
            batchSqlSession.rollback();
            log.info("{}", e.fillInStackTrace());
        } finally {
            batchSqlSession.close();
        }
    }
}
