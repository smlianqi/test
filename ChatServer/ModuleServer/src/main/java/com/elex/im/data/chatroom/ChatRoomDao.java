package com.elex.im.data.chatroom;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;

import com.elex.common.component.data.ADataDao;
import com.elex.common.component.ignite.IIgniteService;

public class ChatRoomDao extends ADataDao<ChatRoom> implements IChatRoomDao {
	private IgniteCache<ChatRoomKey, ChatRoom> cache;

	public ChatRoomDao(IIgniteService igniteService) {
		super(igniteService);
		init();
	}

	private void init() {
		// Create near-cache configuration for "myCache".
		// NearCacheConfiguration<ChatRoomKey, ChatRoom> nearCfg = new
		// NearCacheConfiguration<>();
		// Use LRU eviction policy to automatically evict entries
		// from near-cache, whenever it reaches 100_000 in size.
		// nearCfg.setNearEvictionPolicy(new LruEvictionPolicy<>(100_000));

		CacheConfiguration<ChatRoomKey, ChatRoom> cfg = new CacheConfiguration<>();
		cfg.setName(getPojoClass().getSimpleName().toUpperCase());
		// cfg.setGroupName(ChatRoom.class.getSimpleName());
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
		cfg.setNodeFilter(new ChatRoomNodeFilter());

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
		fields.put("roomType", Integer.class.getName());
		fields.put("admin", String.class.getName());
		fields.put("createTime", Long.class.getName());
		queryEntity.setFields(fields);

		// 索引
		// QueryIndex roomIdIndex = new QueryIndex("roomId");
		// queryEntity.setIndexes(Arrays.asList(roomIdIndex));

		return queryEntity;
	}

	public static DataRegionConfiguration createDataRegionConfiguration(String swapDirectory) {
		DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
		dataRegionConfiguration.setName(ChatRoom.class.getSimpleName());
		dataRegionConfiguration.setPersistenceEnabled(true);
//		dataRegionConfiguration.setSwapPath(swapDirectory + File.separator + ChatRoom.class.getSimpleName());
		return dataRegionConfiguration;
	}

	@Override
	public Class<ChatRoom> getPojoClass() {
		return ChatRoom.class;
	}

	private Class<ChatRoomKey> getKeyClass() {
		return ChatRoomKey.class;
	}

	@Override
	public void insert(ChatRoom entity, Object attach) {
		ChatRoomKey key = new ChatRoomKey();
		key.setRoomId(entity.getRoomId());
		// 同步
		// cache.put(entity.getRoomId(), entity);
		// 异步
		cache.putAsync(key, entity);
	}

	@Override
	public void batchInsert(List<ChatRoom> entitys, Object attach) {
		for (ChatRoom entity : entitys) {
			insert(entity, attach);
		}
	}

	@Override
	public void update(ChatRoom entity, Object attach) {
		insert(entity, attach);
	}

	@Override
	public ChatRoom selectByRoomId(String roomId) {
		ChatRoomKey key = new ChatRoomKey();
		key.setRoomId(roomId);
		return cache.get(key);
	}
}
