package com.elex.im.module.servertest.test.ignite_mysql.chatmessage;

import java.beans.PropertyVetoException;

import javax.cache.configuration.Factory;

import org.apache.ignite.cache.store.CacheStoreSessionListener;
import org.apache.ignite.cache.store.jdbc.CacheJdbcStoreSessionListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class MysqlStoreListenerFactory implements Factory<CacheStoreSessionListener> {
	private static final long serialVersionUID = 1L;

	private String jdbcUrl = "jdbc:mysql://127.0.0.1/chat_test";
	private String user = "root";
	private String password = "1q2w3e4r";
	private String driverClass = "com.mysql.jdbc.Driver";

	@Override
	public CacheStoreSessionListener create() {
		CacheJdbcStoreSessionListener lsnr = new CacheJdbcStoreSessionListener();

		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(driverClass);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(user);
		dataSource.setPassword(password);

		lsnr.setDataSource(dataSource);
		return lsnr;
	}
}
