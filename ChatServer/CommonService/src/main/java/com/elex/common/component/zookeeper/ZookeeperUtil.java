package com.elex.common.component.zookeeper;

import java.util.List;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;
import com.elex.common.util.string.StringUtil;

/**
 * zk工具类
 * 
 * @author mausmars
 * 
 */
public class ZookeeperUtil {
	protected static final ILogger logger = XLogUtil.logger();

	/**
	 * 删除zk节点
	 * 
	 * @param path
	 * @param zk
	 * @param isRoot
	 * @throws Exception
	 */
	public static void cleanAll(String path, ZooKeeper zk, boolean isRoot) throws Exception {
		Stat stat = zk.exists(path, false);
		if (stat == null) {
			if (logger.isInfoEnabled()) {
				logger.info("[node isn't exist " + path + "]");
			}
			return;
		}
		List<String> childerNode = zk.getChildren(path, false);
		for (String childNode : childerNode) {
			if (isRoot) {
				cleanAll(path + childNode, zk, false);
			} else {
				cleanAll(path + StringUtil.SeparatorSlash + childNode, zk, false);
			}
		}
		if (!isRoot) {
			zk.delete(path, -1);
			if (logger.isInfoEnabled()) {
				logger.info("[delete node " + path + "]");
			}
		}
	}

	public static void main(String[] args) {
		DOMConfigurator.configureAndWatch("propertiesconfig/log4j.xml");

		String hosts = "172.16.0.151:2181,172.16.0.141:2182";
		int sessionTimeOut = 1000;
		try {
			ZooKeeper zk = new ZooKeeper(hosts, sessionTimeOut, new Watcher() {
				@Override
				public void process(WatchedEvent arg0) {
				}
			});
			// cleanAll("/", zk, true);
			cleanAll("/sms", zk, false);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
