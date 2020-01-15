package com.elex.common.component.database;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.FileSystemResource;

import com.elex.common.component.database.config.ScDatabase;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据源管理（封装mybatis的session，c3p0数据源）
 * 
 * @author mausmars
 *
 */
public class DatabaseService extends AbstractService<ScDatabase> implements IDatabaseService {
	private ComboPooledDataSource dataSource;
	private SqlSessionFactory sqlSessionFactory;

	private Map<Integer, TransactionIsolationLevel> transactionIsolationLevelMap;

	public DatabaseService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
		transactionIsolationLevelMap = new HashMap<Integer, TransactionIsolationLevel>();
		for (TransactionIsolationLevel til : TransactionIsolationLevel.values()) {
			transactionIsolationLevelMap.put(til.getLevel(), til);
		}
	}

	@Override
	public ISqlSession openSession() {
		return new MybatisSqlSession(sqlSessionFactory.openSession());
	}

	@Override
	public Object openSession(int transactionIsolationLevel) {
		TransactionIsolationLevel til = transactionIsolationLevelMap.get(transactionIsolationLevel);
		if (til == null) {
			throw new IllegalArgumentException();
		}
		return sqlSessionFactory.openSession(til);
	}

	@Override
	public void initService() throws Exception {
		ScDatabase configEntity = getSConfig();
		// 初始化c3p0数据源
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(configEntity.getDriverClass());
		dataSource.setJdbcUrl(configEntity.getJdbcUrl());
		dataSource.setUser(configEntity.getUser());
		dataSource.setPassword(configEntity.getPassword());
		dataSource.setMinPoolSize(configEntity.getMinPoolSize());
		dataSource.setMaxPoolSize(configEntity.getMaxPoolSize());
		dataSource.setInitialPoolSize(configEntity.getInitialPoolSize());
		dataSource.setAcquireIncrement(configEntity.getAcquireIncrement());
		dataSource.setBreakAfterAcquireFailure(configEntity.getBreakAfterAcquireFailure());
		dataSource.setAutoCommitOnClose(configEntity.getAutoCommitOnClose());
		dataSource.setMaxIdleTime(configEntity.getMaxIdleTime());
		dataSource.setMaxStatements(configEntity.getMaxStateMents());
		// 每n秒检查所有连接池中的空闲连接。Default: 0
		dataSource.setIdleConnectionTestPeriod(configEntity.getIdleConnectionTestPeriod());
		dataSource.setPreferredTestQuery(configEntity.getPreferredTestQuery());
		dataSource.setAcquireRetryAttempts(configEntity.getAcquireRetryAttempts());
		dataSource.setAcquireRetryDelay(configEntity.getAcquireRetryDelay());

		// 初始化mybatis的session
		String mybatisSQLMapPath = configEntity.getExtraParamsMap().get(DatabaseConstant.SqlmapConfigPathKey);
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setConfigLocation(new FileSystemResource(mybatisSQLMapPath));
		sqlSessionFactory = sqlSessionFactoryBean.getObject();

		// 创建数据源
		this.dataSource = dataSource;
	}

	@Override
	public void startupService() throws Exception {
	}

	@Override
	public void shutdownService() throws Exception {
		// 关闭数据源
		dataSource.close();
	}
}
