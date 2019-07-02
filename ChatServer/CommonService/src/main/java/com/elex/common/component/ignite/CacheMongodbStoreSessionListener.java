package com.elex.common.component.ignite;

import javax.cache.integration.CacheWriterException;

import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreSession;
import org.apache.ignite.cache.store.CacheStoreSessionListener;
import org.apache.ignite.lifecycle.LifecycleAware;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class CacheMongodbStoreSessionListener implements CacheStoreSessionListener, LifecycleAware {
	/** Data source. */
	private MongoClient dataSrc;
	private String databaseName;

	/**
	 * Sets data source.
	 * <p>
	 * This is a required parameter. If data source is not set, exception will be
	 * thrown on startup.
	 *
	 * @param dataSrc
	 *            Data source.
	 */
	public void setDataSource(MongoClient dataSrc) {
		this.dataSrc = dataSrc;
	}

	/**
	 * Gets data source.
	 *
	 * @return Data source.
	 */
	public MongoClient getDataSource() {
		return dataSrc;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/** {@inheritDoc} */
	@Override
	public void start() throws IgniteException {
		if (dataSrc == null)
			throw new IgniteException("Data source is required by " + getClass().getSimpleName() + '.');
	}

	/** {@inheritDoc} */
	@Override
	public void stop() throws IgniteException {
		// No-op.
	}

	/** {@inheritDoc} */
	@Override
	public void onSessionStart(CacheStoreSession ses) {
		if (ses.attachment() == null) {
			try {
				MongoDatabase database = dataSrc.getDatabase(databaseName);
				ses.attach(database);
			} catch (Exception e) {
				throw new CacheWriterException("Failed to start store session!", e);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onSessionEnd(CacheStoreSession ses, boolean commit) {
		MongoDatabase database = ses.attach(null);
		if (database != null) {
			try {
			} catch (Exception e) {
				throw new CacheWriterException("Failed to end store session!!!", e);
			} finally {
			}
		}
	}
}
