package com.elex.common.component.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import com.elex.common.component.event.IEventService;
import com.elex.common.component.zookeeper.config.ScZookeeper;
import com.elex.common.service.AbstractService;
import com.elex.common.service.IGlobalContext;
import com.elex.common.service.config.IServiceConfig;
import com.elex.common.service.type.ServiceType;
import com.google.common.eventbus.Subscribe;

/**
 * zk 服务器管理； 路径： /root/组id/功能类型/功能key_zk自动生成的id。 例子： /sms/rid/ftype/fkey_00000xx
 * 
 * @author mausmars
 * 
 */
public class ZookeeperService extends AbstractService<ScZookeeper> implements IZookeeperService, Watcher {
	private volatile ZooKeeper zk;
	// --------------------------------
	private volatile CountDownLatch connectedLatch;

	private static final int MAX_RETRIES = 3;
	private static final long RETRY_PERIOD_SECONDS = 20;

	// 权限模板
	private static final String Permissions_Template = "%s:%s";
	private static final String PermissionsType = "digest";
	private List<ACL> permissions;
	private String permissionStr;

	// 事件服务器，用于事件分发
	protected IEventService eventService;

	public ZookeeperService(IServiceConfig sc, IGlobalContext context) {
		super(sc, context);
	}

	@Override
	public void initService() throws Exception {
		ScZookeeper c = getSConfig();

		List<String> eventSids = c.getDependIdsMap().get(ServiceType.event.name());
		eventService = getServiceManager().getService(ServiceType.event, eventSids.get(0));
		
		List<String> permissionsParam = c.getPermissionsList();
		if (permissionsParam != null && !permissionsParam.isEmpty()) {
			permissionStr = String.format(Permissions_Template, permissionsParam.get(0), permissionsParam.get(1));
		}

		createNewZookeeper();

		// 初始化权限
		initPermissions();

		// 注册监听
		eventService.registerSync(this);
	}

	private void createNewZookeeper() throws IOException {
		ScZookeeper c = getSConfig();
		zk = new ZooKeeper(c.getHosts(), c.getTimeout(), this);
		if (permissionStr != null) {
			zk.addAuthInfo(PermissionsType, permissionStr.getBytes());
		}
		// 设置门栓
		connectedLatch = new CountDownLatch(1);
	}

	private void initPermissions() throws Exception {
		ScZookeeper c = getSConfig();
		if (permissionStr != null) {
			// 权限
			List<ACL> permissions = new ArrayList<>();

			List<String> permissionsParam = c.getPermissionsList();
			Id id = new Id(PermissionsType, DigestAuthenticationProvider.generateDigest(permissionStr));

			int perms = 0;
			String type = permissionsParam.get(2);
			switch (type) {
			case "ALL":
				perms = ZooDefs.Perms.ALL;
				break;

			default:
				break;
			}
			ACL acl = new ACL(perms, id);
			permissions.add(acl);

			this.permissions = permissions;
		}
	}

	@Override
	public List<ACL> getPermissions() {
		return permissions;
	}

	@Override
	public void startupService() throws Exception {
		waitUntilConnected();
	}

	@Override
	public void shutdownService() throws Exception {
		zk.close();
	}

	@Override
	public void process(WatchedEvent event) {
		if (logger.isInfoEnabled()) {
			logger.info("[ZookeeperEvent " + event + "]");
		}
		// 事件分发
		eventService.notify(event);
	}

	/**
	 * zk状态变更事件
	 * 
	 * @param event
	 */
	@Subscribe
	public void listen(WatchedEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("EventListener WatchedEvent event=" + event);
		}
		
		String path = event.getPath();
		if (path != null) {
			return;
		}
		switch (event.getState()) {
		case SyncConnected:
			// 已经链接
			connectedLatch.countDown();
			break;
		case Expired:
			// 超时
			reStartup();
		case Disconnected:
			// 链接断开
			return;
		default:
			break;
		}
	}

	private void waitUntilConnected() {
		if (States.CONNECTING == zk.getState()) {
			try {
				connectedLatch.await();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	private void reStartup() {
		Thread thread = new Thread() {
			public void run() {
				for (;;) {
					try {
						// 这里重新创建连接
						if (logger.isInfoEnabled()) {
							logger.info("ZookeeperService reStartup!");
						}
						// 重新创建链接
						createNewZookeeper();
						// 阻塞当前线程
						waitUntilConnected();
						break;
					} catch (Exception e1) {
						logger.error("", e1);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
				}
				// 通知监听重新注册
				// for (ILocalEventListener expiredListener : expiredListeners) {
				// expiredListener.update(null);
				// }
			}
		};
		thread.start();
	}

	// -------------------------------------------------------
	public Stat exists(String path, boolean watch) throws KeeperException, InterruptedException {
		try {
			return zk.exists(path, watch);
		} catch (KeeperException e) {
			throw e;
		}
	}

	public String create(String path, byte data[], List<ACL> acl, CreateMode createMode)
			throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.create(path, data, acl, createMode);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	public List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.getChildren(path, watch);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	public byte[] getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.getData(path, watch, stat);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	public void delete(final String path, int version) throws InterruptedException, KeeperException {
		int retries = 0;
		while (true) {
			try {
				zk.delete(path, version);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	public Stat setData(String path, byte[] data, int version) throws KeeperException, InterruptedException {
		int retries = 0;
		while (true) {
			try {
				return zk.setData(path, data, version);
			} catch (KeeperException.SessionExpiredException e) {
				throw e;
			} catch (KeeperException e) {
				if (retries++ == MAX_RETRIES) {
					throw e;
				}
				// sleep then retry
				Thread.sleep(RETRY_PERIOD_SECONDS);
			}
		}
	}

	public void setData(String path, byte[] data, int version, StatCallback cb, Object ctx) {
		zk.setData(path, data, version, cb, ctx);
	}
}
