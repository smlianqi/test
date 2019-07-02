/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elex.im.module.servertest.test.ignite_mysql.chatmessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.cache.store.CacheStoreSession;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.CacheStoreSessionResource;

import com.elex.common.util.log.ILogger;
import com.elex.common.util.log.XLogUtil;

public class ChatMessageCacheJdbcStore extends CacheStoreAdapter<ChatMessageKey, ChatMessage> {
	protected static final ILogger logger = XLogUtil.logger();

	/** Store session. */
	@CacheStoreSessionResource
	private CacheStoreSession ses;

	// 插入sql
	private String InsertSql = "INSERT INTO `chat_message` (`roomId`,`orderId`,`uid`,`contentType`,`content`,`atUids`,`sendedTime`,`receivedTime`,`clientExt`,`serverExt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
	// 更新sql
	private String UpdateSql = "UPDATE `chat_message` SET `roomId`=?,`orderId`=?,`uid`=?,`contentType`=?,`content`=?,`atUids`=?,`sendedTime`=?,`receivedTime`=?,`clientExt`=?,`serverExt`=? WHERE (`roomId`=? and `orderId`=?)";
	// 删除sql
	private String RemoveSql = "DELETE FROM `chat_message`  WHERE (`roomId`=? and `orderId`=?)";
	// 创建表sql
	private String CreateTableSql = "CREATE TABLE `chat_message` ("
			+ "`roomId` varchar(255) NOT NULL comment '房间id(全服固定id，用户组合id，工会id)',"
			+ "`orderId` bigint NOT NULL comment '房间消息序列'," + "`uid` varchar(255) NOT NULL comment '所属用户',"
			+ "`contentType` int NOT NULL comment '消息类型'," + "`content` varchar(2000) NOT NULL comment '消息内容',"
			+ "`atUids` varchar(2000) NOT NULL comment '@的用户id，可以为空',"
			+ "`sendedTime` bigint NOT NULL comment '客户端发送消息时的时间（客户端生成）',"
			+ "`receivedTime` bigint NOT NULL comment '服务端接收到消息时的时间 （服务端生成）',"
			+ "`clientExt` varchar(2000) NOT NULL comment '消息内容',"
			+ "`serverExt` varchar(2000) NOT NULL comment '消息内容'," + "PRIMARY KEY (`roomId`,`orderId`)"
			+ ")ENGINE=InnoDB DEFAULT CHARSET=utf8";

	private String SelectRoomIdOrderSql = "SELECT * FROM `chat_message` WHERE (`roomId`=? and `orderId`=?)";
	private String SelectSql = "SELECT * FROM chat_message LIMIT ?";

	private void createTable() {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store createTable ChatMessage");
		}
		Connection conn = ses.attachment();
		Statement ps = null;
		try {
			ps = conn.createStatement();
			ps.execute(CreateTableSql);
		} catch (SQLException e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	public void setSes(CacheStoreSession ses) {
		this.ses = ses;
		createTable();
	}

	/** {@inheritDoc} */
	@Override
	public ChatMessage load(ChatMessageKey key) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store load ChatMessage [key=" + key + ']');
		}
		Connection conn = ses.attachment();
		List<ChatMessage> pojoList = null;
		try (PreparedStatement st = conn.prepareStatement(SelectRoomIdOrderSql)) {
			st.setString(1, key.getRoomId());
			st.setLong(2, key.getOrderId());

			ResultSet rs = st.executeQuery();
			pojoList = getPoJoList(rs);
			return (pojoList == null || pojoList.isEmpty()) ? null : pojoList.get(0);
		} catch (SQLException e) {
			logger.error("", e);
			throw new CacheLoaderException("Failed to load object [key=" + key + ']', e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void loadCache(IgniteBiInClosure<ChatMessageKey, ChatMessage> clo, Object... args) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store loadCache ChatMessage");
		}
		if (args == null || args.length == 0 || args[0] == null)
			throw new CacheLoaderException("Expected entry count parameter is not provided.");
		ChatMessageKey key = (ChatMessageKey) args[0];
		Connection conn = ses.attachment();
		try (PreparedStatement ps = conn.prepareStatement(SelectSql)) {
			ps.setString(1, key.getRoomId());
			ps.setLong(2, key.getOrderId());
			ResultSet rs = ps.executeQuery();

			List<ChatMessage> pojoList = getPoJoList(rs);
			for (ChatMessage pojo : pojoList) {
				ChatMessageKey k = new ChatMessageKey();
				k.setOrderId(pojo.getOrderId());
				k.setRoomId(pojo.getRoomId());

				clo.apply(k, pojo);
			}
		} catch (SQLException e) {
			logger.error("", e);
			throw new CacheLoaderException("Failed to load values from cache store.", e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void write(Cache.Entry<? extends ChatMessageKey, ? extends ChatMessage> entry) {
		ChatMessageKey key = entry.getKey();
		ChatMessage pojo = entry.getValue();
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store write ChatMessage [key=" + key + ", val=" + pojo + ']');
		}
		try {
			Connection conn = ses.attachment();
			int updated;
			// Try update first. If it does not work, then try insert.
			// Some databases would allow these to be done in one 'upsert' operation.
			try (PreparedStatement st = conn.prepareStatement(UpdateSql)) {
				setUpdateParams(st, pojo);
				updated = st.executeUpdate();
			}

			// If update failed, try to insert.
			if (updated == 0) {
				try (PreparedStatement st = conn.prepareStatement(InsertSql)) {
					setInsertParams(st, pojo);
					st.executeUpdate();
				}
			}
		} catch (SQLException e) {
			logger.error("", e);
			throw new CacheWriterException("Failed to write object [key=" + key + ", pojo=" + pojo + ']', e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void delete(Object key) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store delete ChatMessage [key=" + key + ']');
		}
		Connection conn = ses.attachment();
		try (PreparedStatement st = conn.prepareStatement(RemoveSql)) {
			st.setLong(1, (Long) key);

			st.executeUpdate();
		} catch (SQLException e) {
			logger.error("", e);
			throw new CacheWriterException("Failed to delete object [key=" + key + ']', e);
		}
	}

	protected List<ChatMessage> getPoJoList(ResultSet rs) throws SQLException {
		List<ChatMessage> pojoList = new LinkedList<ChatMessage>();

		while (rs.next()) {
			ChatMessage pojo = new ChatMessage();
			pojo.setRoomId(rs.getString("roomId"));
			pojo.setOrderId(rs.getLong("orderId"));
			pojo.setUid(rs.getString("uid"));
			pojo.setContentType(rs.getInt("contentType"));
			pojo.setContent(rs.getString("content"));
			pojo.setAtUids(rs.getString("atUids"));
			pojo.setSendedTime(rs.getLong("sendedTime"));
			pojo.setReceivedTime(rs.getLong("receivedTime"));
			pojo.setClientExt(rs.getString("clientExt"));
			pojo.setServerExt(rs.getString("serverExt"));
			pojoList.add(pojo);
		}
		return pojoList;
	}

	protected void setInsertParams(PreparedStatement ps, ChatMessage pojo) throws SQLException {
		ps.setString(1, pojo.getRoomId());
		ps.setLong(2, pojo.getOrderId());
		ps.setString(3, pojo.getUid());
		ps.setInt(4, pojo.getContentType());
		ps.setString(5, pojo.getContent());
		ps.setString(6, pojo.getAtUids());
		ps.setLong(7, pojo.getSendedTime());
		ps.setLong(8, pojo.getReceivedTime());
		ps.setString(9, pojo.getClientExt());
		ps.setString(10, pojo.getServerExt());
	}

	protected void setUpdateParams(PreparedStatement ps, ChatMessage pojo) throws SQLException {
		ps.setString(1, pojo.getRoomId());
		ps.setLong(2, pojo.getOrderId());
		ps.setString(3, pojo.getUid());
		ps.setInt(4, pojo.getContentType());
		ps.setString(5, pojo.getContent());
		ps.setString(6, pojo.getAtUids());
		ps.setLong(7, pojo.getSendedTime());
		ps.setLong(8, pojo.getReceivedTime());
		ps.setString(9, pojo.getClientExt());
		ps.setString(10, pojo.getServerExt());
		ps.setString(11, pojo.getRoomId());
		ps.setLong(12, pojo.getOrderId());
	}

	protected void setRemoveParams(PreparedStatement ps, ChatMessage pojo) throws SQLException {
		ps.setString(1, pojo.getRoomId());
		ps.setLong(2, pojo.getOrderId());
	}

	protected void close(Connection con, Statement ps, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("", e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				logger.error("", e);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				logger.error("", e);
			}
		}
	}
}
