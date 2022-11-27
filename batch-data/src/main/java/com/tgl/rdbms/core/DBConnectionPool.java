package com.tgl.rdbms.core;

import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionPool {
	private DruidDataSource dds = null;
	private static DBConnectionPool instance;
	private DBConnectionPool() {
		try {
			log.info("初始化连接池");
			dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(DBconfig.properties);
			dds.init();
		} catch (Exception e) {
			log.error("数据库连接池初始化失败！！！", e);
		}
	}

	public static DBConnectionPool getInstance() {
		if (instance == null) {
			instance = new DBConnectionPool();
		}
		return instance;
	}
	private static DruidDataSource getDruidDataSource(){
		return getInstance().dds;
	}
	public static DruidPooledConnection getConnection() throws SQLException{
		return getDruidDataSource().getConnection();
	}
}
