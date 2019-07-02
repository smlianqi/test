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

package com.elex.im.data.chatmessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private String InsertSql = "INSERT INTO `chat_message` (`roomId`,`orderId`,`uid`,`content`,`atUids`,`sendedTime`,`receivedTime`) VALUES (?,?,?,?,?,?,?)";
	// 更新sql
	private String UpdateSql = "UPDATE `chat_message` SET `roomId`=?,`orderId`=?,`uid`=?,`content`=?,`atUids`=?,`sendedTime`=?,`receivedTime`=? WHERE (`roomId`=? and `orderId`=?)";
	// 删除sql
	private String RemoveSql = "DELETE FROM `chat_message`  WHERE (`roomId`=? and `orderId`=?)";

	private String SelectRoomIdOrderSql="SELECT * FROM `chat_message` WHERE (`roomId`=? and `orderId`=?)";
	private String SelectSql = "SELECT * FROM chat_message LIMIT ?";

	/** {@inheritDoc} */
	@Override
	public ChatMessage load(ChatMessageKey key) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store load [key=" + key + ']');
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
			throw new CacheLoaderException("Failed to load object [key=" + key + ']', e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void write(Cache.Entry<? extends ChatMessageKey, ? extends ChatMessage> entry) {
		ChatMessageKey key = entry.getKey();
		ChatMessage pojo = entry.getValue();
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store write [key=" + key + ", val=" + pojo + ']');
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
			throw new CacheWriterException("Failed to write object [key=" + key + ", pojo=" + pojo + ']', e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void delete(Object key) {
		if (logger.isDebugEnabled()) {
			logger.debug(">>> Store delete [key=" + key + ']');
		}
		Connection conn = ses.attachment();
		try (PreparedStatement st = conn.prepareStatement(RemoveSql)) {
			st.setLong(1, (Long) key);

			st.executeUpdate();
		} catch (SQLException e) {
			throw new CacheWriterException("Failed to delete object [key=" + key + ']', e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void loadCache(IgniteBiInClosure<ChatMessageKey, ChatMessage> clo, Object... args) {
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
			throw new CacheLoaderException("Failed to load values from cache store.", e);
		}
	}

	protected List<ChatMessage> getPoJoList(ResultSet rs) throws SQLException {
		List<ChatMessage> pojoList = new LinkedList<ChatMessage>();

		while (rs.next()) {
			ChatMessage pojo = new ChatMessage();
			pojo.setRoomId(rs.getString("roomId"));
			pojo.setOrderId(rs.getLong("orderId"));
			pojo.setUid(rs.getString("uid"));
			pojo.setContent(rs.getString("content"));
			// pojo.setAtUids(rs.getString("atUids"));
			pojo.setSendedTime(rs.getLong("sendedTime"));
			pojo.setReceivedTime(rs.getLong("receivedTime"));
			pojoList.add(pojo);
		}
		return pojoList;
	}

	protected void setInsertParams(PreparedStatement ps, ChatMessage pojo) throws SQLException {
		ps.setString(1, pojo.getRoomId());
		ps.setLong(2, pojo.getOrderId());
		ps.setString(3, pojo.getUid());
		ps.setString(4, pojo.getContent());
		// ps.setString(5, pojo.getAtUids());
		ps.setLong(6, pojo.getSendedTime());
		ps.setLong(7, pojo.getReceivedTime());
	}

	protected void setUpdateParams(PreparedStatement ps, ChatMessage pojo) throws SQLException {
		ps.setString(1, pojo.getRoomId());
		ps.setLong(2, pojo.getOrderId());
		ps.setString(3, pojo.getUid());
		ps.setString(4, pojo.getContent());
		// ps.setString(5, pojo.getAtUids());
		ps.setLong(6, pojo.getSendedTime());
		ps.setLong(7, pojo.getReceivedTime());
		ps.setString(8, pojo.getRoomId());
		ps.setLong(9, pojo.getOrderId());
	}

	protected void setRemoveParams(PreparedStatement ps, ChatMessage pojo) throws SQLException {
		ps.setString(1, pojo.getRoomId());
		ps.setLong(2, pojo.getOrderId());
	}
}
