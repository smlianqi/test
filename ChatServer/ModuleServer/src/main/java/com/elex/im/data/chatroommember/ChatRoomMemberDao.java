package com.elex.im.data.chatroommember;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.cache.Cache.Entry;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;

import com.elex.common.component.data.ADataDao;
import com.elex.common.component.ignite.IIgniteService;

public class ChatRoomMemberDao extends ADataDao<ChatRoomMember> implements IChatRoomMemberDao {
	private IgniteCache<ChatRoomMemberKey, ChatRoomMember> cache;

	public ChatRoomMemberDao(IIgniteService igniteService) {
		super(igniteService);
		init();
	}

	private void init() {
		// Create near-cache configuration for "myCache".
		// NearCacheConfiguration<ChatRoomMemberKey, ChatRoomMember> nearCfg = new
		// NearCacheConfiguration<>();
		// Use LRU eviction policy to automatically evict entries
		// from near-cache, whenever it reaches 100_000 in size.
		// nearCfg.setNearEvictionPolicy(new LruEvictionPolicy<>(100_000));

		CacheConfiguration<ChatRoomMemberKey, ChatRoomMember> cfg = new CacheConfiguration<>();
		cfg.setName(getPojoClass().getSimpleName().toUpperCase());
		// cfg.setGroupName(ChatRoomMember.class.getSimpleName());
		// 分区模式 LOCAL, REPLICATED, PARTITIONED
		cfg.setCacheMode(CacheMode.REPLICATED);// 设置有每个服务器都有完整备份
		// TRANSACTIONAL, ATOMIC原子有序写模式
		cfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
		// cfg.setAtomicWriteOrderMode(CacheAtomicWriteOrderMode.CLOCK);
		cfg.setBackups(1);
		// FULL_SYNC, FULL_ASYNC, PRIMARY_SYNC
		cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
		// cfg.setNearConfiguration(nearCfg);// 近缓存
		cfg.setCopyOnRead(false);
		cfg.setOnheapCacheEnabled(true);
		// cfg.setInvalidate(true);// Invalidate near cache

		cfg.setQueryEntities(Arrays.asList(createQueryEntity()));

		CacheKeyConfiguration cacheKeyConfiguration = new CacheKeyConfiguration(getKeyClass());
		cacheKeyConfiguration.setAffinityKeyFieldName("roomId");
		cfg.setKeyConfiguration(cacheKeyConfiguration);

		// 设置数据分区设置
		cfg.setDataRegionName(getPojoClass().getSimpleName());

		// 添加过滤
		cfg.setNodeFilter(new ChatRoomMemberNodeFilter());

		// Create cache with given name, if it does not exist.
		// 异步支持,原子操作,EntryProcessor
		cache = igniteService.getIgnite().getOrCreateCache(cfg);
	}

	private QueryEntity createQueryEntity() {
		QueryEntity queryEntity = new QueryEntity();

		// key-value类型
		queryEntity.setKeyType(getKeyClass().getName());
		queryEntity.setValueType(getPojoClass().getName());

		// 域类型
		LinkedHashMap<String, String> fields = new LinkedHashMap<>();
		fields.put("roomId", String.class.getName());
		fields.put("uid", String.class.getName());
		fields.put("lastOrder", Long.class.getName());
		fields.put("atOrders", List.class.getName());
		fields.put("state", Integer.class.getName());
		queryEntity.setFields(fields);

		// 索引
		// QueryIndex roomIdIndex = new QueryIndex("roomId");
		QueryIndex uidIndex = new QueryIndex("uid");
		queryEntity.setIndexes(Arrays.asList(uidIndex));

		return queryEntity;
	}

	public static DataRegionConfiguration createDataRegionConfiguration(String swapDirectory) {
		DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
		dataRegionConfiguration.setName(ChatRoomMember.class.getSimpleName());
		dataRegionConfiguration.setPersistenceEnabled(true);
//		dataRegionConfiguration.setSwapPath(swapDirectory + File.separator + ChatRoomMember.class.getSimpleName());
		return dataRegionConfiguration;
	}

	@Override
	public Class<ChatRoomMember> getPojoClass() {
		return ChatRoomMember.class;
	}

	private Class<ChatRoomMemberKey> getKeyClass() {
		return ChatRoomMemberKey.class;
	}

	@Override
	public void insert(ChatRoomMember entity, Object attach) {
		ChatRoomMemberKey key = new ChatRoomMemberKey();
		key.setRoomId(entity.getRoomId());
		key.setUid(entity.getUid());
		// cache.put(key, entity);
		// 异步
		cache.putAsync(key, entity);
	}

	@Override
	public void batchInsert(List<ChatRoomMember> entitys, Object attach) {
		for (ChatRoomMember entity : entitys) {
			insert(entity, attach);
		}
	}

	@Override
	public void update(ChatRoomMember entity, Object attach) {
		insert(entity, attach);
	}

	@Override
	public ChatRoomMember selectByRoomIdAndUid(String roomId, String uid) {
		ChatRoomMemberKey key = new ChatRoomMemberKey();
		key.setRoomId(roomId);
		key.setUid(uid);
		return cache.get(key);
	}

	private String uidSql = "uid = ?";

	@Override
	public List<ChatRoomMember> selectByUid(String uid) {
		SqlQuery<ChatRoomMemberKey, ChatRoomMember> sqlQuery = new SqlQuery<>(ChatRoomMember.class, uidSql);
		sqlQuery.setArgs(uid);
		List<Entry<ChatRoomMemberKey, ChatRoomMember>> entrys = cache.query(sqlQuery).getAll();

		List<ChatRoomMember> entitys = new LinkedList<>();
		for (Entry<ChatRoomMemberKey, ChatRoomMember> entry : entrys) {
			entitys.add(entry.getValue());
		}
		return entitys;
	}

	private String roomIdSql = "roomId = ?";

	@Override
	public List<ChatRoomMember> selectByRoomId(String roomId) {
		SqlQuery<ChatRoomMemberKey, ChatRoomMember> sqlQuery = new SqlQuery<>(ChatRoomMember.class, roomIdSql);
		sqlQuery.setArgs(roomId);
		List<Entry<ChatRoomMemberKey, ChatRoomMember>> entrys = cache.query(sqlQuery).getAll();

		List<ChatRoomMember> entitys = new LinkedList<>();
		for (Entry<ChatRoomMemberKey, ChatRoomMember> entry : entrys) {
			entitys.add(entry.getValue());
		}
		return entitys;
	}
}
