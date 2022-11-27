package com.tgl.rdbms.core;

import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DBconfig {

	public static Properties properties=new Properties();

	@Value("${DBLink.name}")
	private String name;
	@Value("${DBLink.url}")
	private String url;
	@Value("${DBLink.username}")
	private String username;
	@Value("${DBLink.password}")
	private String password;
	@Value("${DBLink.driverClassName}")
	private String driverClassName;
	@Value("${DBLink.initialSize}")
	private String initialSize;
	@Value("${DBLink.maxActive}")
	private String maxActive;
	@Value("${DBLink.minIdle}")
	private String minIdle;
	@Value("${DBLink.useUnfairLock}")
	private String useUnfairLock;
	@Value("${DBLink.maxWait}")
	private String maxWait;
	@Value("${DBLink.poolPreparedStatements}")
	private String poolPreparedStatements;
	@Value("${DBLink.maxOpenPreparedStatements}")
	private String maxOpenPreparedStatements;
	@Value("${DBLink.maxPoolPreparedStatementsPerConnectionSize}")
	private String maxPoolPreparedStatementsPerConnectionSize;
	@Value("${DBLink.validationQuery}")
	private String validationQuery;
	@Value("${DBLink.validationQueryTimeout}")
	private String validationQueryTimeout;
	@Value("${DBLink.testOnBorrow}")
	private String testOnBorrow;
	@Value("${DBLink.testOnReturn}")
	private String testOnReturn;
	@Value("${DBLink.testWhileIdle}")
	private String testWhileIdle;
	@Value("${DBLink.timeBetweenEvictionRunsMillis}")
	private String timeBetweenEvictionRunsMillis;
	@Value("${DBLink.minEvictableIdleTimeMillis}")
	private String minEvictableIdleTimeMillis;
	@Value("${DBLink.connectionInitSqls}")
	private String connectionInitSqls;
	@Value("${DBLink.exceptionSorter}")
	private String exceptionSorter;
	@Value("${DBLink.filters}")
	private String filters;
	@Value("${DBLink.removeAbandoned}")
	private String removeAbandoned;
	@Value("${DBLink.removeAbandonedTimeout}")
	private String removeAbandonedTimeout;
	@Value("${DBLink.logAbandoned}")
	private String logAbandoned;
	@Value("${DBLink.queryTimeout}")
	private String queryTimeout;
	@Value("${DBLink.transactionQueryTimeout}")
	private String transactionQueryTimeout;

	@PostConstruct
	public void setProperties() throws SQLException {
		log.info("开始读取数据库配置信息");
		properties.setProperty("name", name);
		properties.setProperty("url", url);
		properties.setProperty("username", username);
		properties.setProperty("password", password);
		properties.setProperty("driverClassName", driverClassName);
		properties.setProperty("initialSize", initialSize);
		properties.setProperty("maxActive", maxActive);
		properties.setProperty("minIdle", minIdle);
		properties.setProperty("useUnfairLock", useUnfairLock);
		properties.setProperty("maxWait", maxWait);
		properties.setProperty("poolPreparedStatements", poolPreparedStatements);
		properties.setProperty("maxOpenPreparedStatements", maxOpenPreparedStatements);
		properties.setProperty("maxPoolPreparedStatementsPerConnectionSize", maxPoolPreparedStatementsPerConnectionSize);
		properties.setProperty("validationQuery", validationQuery);
		properties.setProperty("validationQueryTimeout", validationQueryTimeout);
		properties.setProperty("testOnBorrow", testOnBorrow);
		properties.setProperty("testOnReturn", testOnReturn);
		properties.setProperty("testWhileIdle", testWhileIdle);
		properties.setProperty("timeBetweenEvictionRunsMillis", timeBetweenEvictionRunsMillis);
		properties.setProperty("minEvictableIdleTimeMillis", minEvictableIdleTimeMillis);
		properties.setProperty("connectionInitSqls", connectionInitSqls);
		properties.setProperty("exceptionSorter", exceptionSorter);
		properties.setProperty("filters", filters);
		properties.setProperty("removeAbandoned", removeAbandoned);
		properties.setProperty("removeAbandonedTimeout", removeAbandonedTimeout);
		properties.setProperty("logAbandoned", logAbandoned);
		properties.setProperty("queryTimeout", queryTimeout);
		properties.setProperty("transactionQueryTimeout", transactionQueryTimeout);
		log.info("成功读取数据库配置信息");
		DBConnectionPool.getInstance();
		DBConnectionPool.getConnection().close();
	}
}
