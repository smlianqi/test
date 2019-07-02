package com.elex.common.component.zookeeper;

import java.util.List;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import com.elex.common.service.IService;

/**
 * zk服务
 * 
 * @author mausmars
 *
 */
public interface IZookeeperService extends IService {
	List<ACL> getPermissions();

	// --------------------------------
	// 封装zk方法
	Stat exists(String path, boolean watch) throws KeeperException, InterruptedException;

	String create(String path, byte data[], List<ACL> acl, CreateMode createMode)
			throws KeeperException, InterruptedException;

	List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException;

	byte[] getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException;

	void delete(final String path, int version) throws InterruptedException, KeeperException;

	Stat setData(String path, byte[] data, int version) throws KeeperException, InterruptedException;

	void setData(String path, byte[] data, int version, StatCallback cb, Object ctx);
}
