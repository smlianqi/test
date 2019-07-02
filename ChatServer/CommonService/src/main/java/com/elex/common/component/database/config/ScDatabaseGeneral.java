package com.elex.common.component.database.config;

import java.util.Date;
import java.util.Map;

import com.elex.common.component.data.ISpread;
import com.elex.common.component.io.ByteArrayInput;
import com.elex.common.component.io.ByteArrayOutput;
import com.elex.common.component.io.IDataInput;
import com.elex.common.component.io.IDataOutput;
import com.elex.common.component.io.IWriteReadable;

import java.io.IOException;

/**
 * 数据库配置(类型101) 实体类
 */
public class ScDatabaseGeneral implements ISpread, IWriteReadable {
	/**
	 * 唯一id
	 */
	protected String id;
	
	/**
	 * 依赖服务
	 */
	protected String dependIds;
	
	/**
	 * 备注
	 */
	protected String readme;
	
	/**
	 * 扩展配置
	 */
	protected String extraParams;
	
	/**
	 * 驱动class
	 */
	protected String driverClass;
	
	/**
	 * 数据库url
	 */
	protected String jdbcUrl;
	
	/**
	 * 用户名
	 */
	protected String user;
	
	/**
	 * 密码
	 */
	protected String password;
	
	/**
	 * 最大连接池
	 */
	protected int maxPoolSize;
	
	/**
	 * 最小连接池
	 */
	protected int minPoolSize;
	
	/**
	 * 初始化连接池大小
	 */
	protected int initialPoolSize;
	
	/**
	 * 最大空闲时间,n秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0
	 */
	protected int maxIdleTime;
	
	/**
	 * 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3
	 */
	protected int acquireIncrement;
	
	/**
	 * 预缓存的statements属于单个 connection而不是整个连接池
	 */
	protected int maxStateMents;
	
	/**
	 * 设为true，那么在尝试获取连接失败后该数据源将申明已断开并永久关闭
	 */
	protected boolean breakAfterAcquireFailure;
	
	/**
	 * 连接关闭时默认将所有未提交的操作回滚。Default: false
	 */
	protected boolean autoCommitOnClose;
	
	/**
	 * 每n秒检查所有连接池中的空闲连接。Default: 0
	 */
	protected int idleConnectionTestPeriod;
	
	/**
	 * 
	 */
	protected String preferredTestQuery;
	
	/**
	 * 连接池在获得新连接失败时重试的次数，如果小于等于0则无限重试直至连接获得成功
	 */
	protected int acquireRetryAttempts;
	
	/**
	 * 两次连接中间隔时间，单位毫秒，连接池在获得新连接时的间隔时间
	 */
	protected int acquireRetryDelay;
	
	@Override
	public void write(IDataOutput out) throws IOException {
			out.writeUTF(id);
			out.writeUTF(dependIds);
			out.writeUTF(readme);
			out.writeUTF(extraParams);
			out.writeUTF(driverClass);
			out.writeUTF(jdbcUrl);
			out.writeUTF(user);
			out.writeUTF(password);
			out.writeInt(maxPoolSize);
			out.writeInt(minPoolSize);
			out.writeInt(initialPoolSize);
			out.writeInt(maxIdleTime);
			out.writeInt(acquireIncrement);
			out.writeInt(maxStateMents);
			out.writeBoolean(breakAfterAcquireFailure);
			out.writeBoolean(autoCommitOnClose);
			out.writeInt(idleConnectionTestPeriod);
			out.writeUTF(preferredTestQuery);
			out.writeInt(acquireRetryAttempts);
			out.writeInt(acquireRetryDelay);
	}

	@Override
	public void read(IDataInput in) throws IOException {
			id=in.readUTF();
			dependIds=in.readUTF();
			readme=in.readUTF();
			extraParams=in.readUTF();
			driverClass=in.readUTF();
			jdbcUrl=in.readUTF();
			user=in.readUTF();
			password=in.readUTF();
			maxPoolSize=in.readInt();
			minPoolSize=in.readInt();
			initialPoolSize=in.readInt();
			maxIdleTime=in.readInt();
			acquireIncrement=in.readInt();
			maxStateMents=in.readInt();
			breakAfterAcquireFailure=in.readBoolean();
			autoCommitOnClose=in.readBoolean();
			idleConnectionTestPeriod=in.readInt();
			preferredTestQuery=in.readUTF();
			acquireRetryAttempts=in.readInt();
			acquireRetryDelay=in.readInt();
	}
	
	@Override
	public byte[] getBytes() throws IOException {
		ByteArrayOutput out = new ByteArrayOutput();
		write(out);
		return out.toByteArray();
	}

	@Override
	public void initField(byte[] bytes) throws IOException {
		ByteArrayInput byteArrayInput = new ByteArrayInput(bytes);
		this.read(byteArrayInput);
	}
	
	@Override
	public void obtainAfter() {
	}

	@Override
	public void saveBefore() {
	}

	@Override
	public void saveAfter() {
	}

	@Override
	public <T> T cloneEntity(boolean isSaveBefore) {
		return null;
	}
	
	@Override
	public Map<String, Object> getIndexChangeBefore() {
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("ScDatabaseGeneral [");
		sb.append("id=");
		sb.append(id);
		sb.append(", ");
		sb.append("dependIds=");
		sb.append(dependIds);
		sb.append(", ");
		sb.append("readme=");
		sb.append(readme);
		sb.append(", ");
		sb.append("extraParams=");
		sb.append(extraParams);
		sb.append(", ");
		sb.append("driverClass=");
		sb.append(driverClass);
		sb.append(", ");
		sb.append("jdbcUrl=");
		sb.append(jdbcUrl);
		sb.append(", ");
		sb.append("user=");
		sb.append(user);
		sb.append(", ");
		sb.append("password=");
		sb.append(password);
		sb.append(", ");
		sb.append("maxPoolSize=");
		sb.append(maxPoolSize);
		sb.append(", ");
		sb.append("minPoolSize=");
		sb.append(minPoolSize);
		sb.append(", ");
		sb.append("initialPoolSize=");
		sb.append(initialPoolSize);
		sb.append(", ");
		sb.append("maxIdleTime=");
		sb.append(maxIdleTime);
		sb.append(", ");
		sb.append("acquireIncrement=");
		sb.append(acquireIncrement);
		sb.append(", ");
		sb.append("maxStateMents=");
		sb.append(maxStateMents);
		sb.append(", ");
		sb.append("breakAfterAcquireFailure=");
		sb.append(breakAfterAcquireFailure);
		sb.append(", ");
		sb.append("autoCommitOnClose=");
		sb.append(autoCommitOnClose);
		sb.append(", ");
		sb.append("idleConnectionTestPeriod=");
		sb.append(idleConnectionTestPeriod);
		sb.append(", ");
		sb.append("preferredTestQuery=");
		sb.append(preferredTestQuery);
		sb.append(", ");
		sb.append("acquireRetryAttempts=");
		sb.append(acquireRetryAttempts);
		sb.append(", ");
		sb.append("acquireRetryDelay=");
		sb.append(acquireRetryDelay);
		
		sb.append("]");
		return sb.toString();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDependIds() {
		return dependIds;
	}
	
	public void setDependIds(String dependIds) {
		this.dependIds = dependIds;
	}
	
	public String getReadme() {
		return readme;
	}
	
	public void setReadme(String readme) {
		this.readme = readme;
	}
	
	public String getExtraParams() {
		return extraParams;
	}
	
	public void setExtraParams(String extraParams) {
		this.extraParams = extraParams;
	}
	
	public String getDriverClass() {
		return driverClass;
	}
	
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getMaxPoolSize() {
		return maxPoolSize;
	}
	
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	
	public int getMinPoolSize() {
		return minPoolSize;
	}
	
	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}
	
	public int getInitialPoolSize() {
		return initialPoolSize;
	}
	
	public void setInitialPoolSize(int initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}
	
	public int getMaxIdleTime() {
		return maxIdleTime;
	}
	
	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
	
	public int getAcquireIncrement() {
		return acquireIncrement;
	}
	
	public void setAcquireIncrement(int acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}
	
	public int getMaxStateMents() {
		return maxStateMents;
	}
	
	public void setMaxStateMents(int maxStateMents) {
		this.maxStateMents = maxStateMents;
	}
	
	public boolean getBreakAfterAcquireFailure() {
		return breakAfterAcquireFailure;
	}
	
	public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
		this.breakAfterAcquireFailure = breakAfterAcquireFailure;
	}
	
	public boolean getAutoCommitOnClose() {
		return autoCommitOnClose;
	}
	
	public void setAutoCommitOnClose(boolean autoCommitOnClose) {
		this.autoCommitOnClose = autoCommitOnClose;
	}
	
	public int getIdleConnectionTestPeriod() {
		return idleConnectionTestPeriod;
	}
	
	public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
		this.idleConnectionTestPeriod = idleConnectionTestPeriod;
	}
	
	public String getPreferredTestQuery() {
		return preferredTestQuery;
	}
	
	public void setPreferredTestQuery(String preferredTestQuery) {
		this.preferredTestQuery = preferredTestQuery;
	}
	
	public int getAcquireRetryAttempts() {
		return acquireRetryAttempts;
	}
	
	public void setAcquireRetryAttempts(int acquireRetryAttempts) {
		this.acquireRetryAttempts = acquireRetryAttempts;
	}
	
	public int getAcquireRetryDelay() {
		return acquireRetryDelay;
	}
	
	public void setAcquireRetryDelay(int acquireRetryDelay) {
		this.acquireRetryDelay = acquireRetryDelay;
	}
	
}