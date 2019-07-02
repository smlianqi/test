package com.elex.im.data.roomuseronline;

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

public class RoomUserOnlineDao extends ADataDao<RoomUserOnline> implements IRoomUserOnlineDao {
	private IgniteCache<RoomUserOnlineKey, RoomUserOnline> cache;

	public RoomUserOnlineDao(IIgniteService igniteService) {
		super(igniteService);
		init();
	}

	private void init() {
		// Create near-cache configuration for "myCache".
		// NearCacheConfiguration<RoomUserOnlineKey, RoomUserOnline> nearCfg = new
		// NearCacheConfiguration<>();
		// Use LRU eviction policy to automatically evict entries
		// from near-cache, whenever it reaches 100_000 in size.
		// nearCfg.setNearEvictionPolicy(new LruEvictionPolicy<>(100_000));

		CacheConfiguration<RoomUserOnlineKey, RoomUserOnline> cfg = new CacheConfiguration<>();
		cfg.setName(getPojoClass().getSimpleName().toUpperCase());
		// cfg.setGroupName(RoomUserOnline.class.getSimpleName());
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
		// cfg.setDataRegionName(getPojoClass().getSimpleName());

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
		queryEntity.setFields(fields);

		// 索引
		// QueryIndex roomIdIndex = new QueryIndex("roomId");
		QueryIndex uidIndex = new QueryIndex("uid");
		queryEntity.setIndexes(Arrays.asList(uidIndex));

		return queryEntity;
	}

	public static DataRegionConfiguration createDataRegionConfiguration(String swapDirectory) {
		DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
		dataRegionConfiguration.setPersistenceEnabled(true);
		dataRegionConfiguration.setName(RoomUserOnline.class.getSimpleName());
		dataRegionConfiguration.setSwapPath(swapDirectory + File.separator + RoomUserOnline.class.getSimpleName());
		return dataRegionConfiguration;
	}

	@Override
	public Class<RoomUserOnline> getPojoClass() {
		return RoomUserOnline.class;
	}

	private Class<RoomUserOnlineKey> getKeyClass() {
		return RoomUserOnlineKey.class;
	}

	@Override
	public void insert(RoomUserOnline entity, Object attach) {
		RoomUserOnlineKey key = new RoomUserOnlineKey();
		key.setRoomId(entity.getRoomId());
		key.setUid(entity.getUid());
		// 插入
		// cache.put(key, entity);
		// 异步
		cache.putAsync(key, entity);
	}

	@Override
	public void remove(RoomUserOnline entity, Object attach) {
		RoomUserOnlineKey key = new RoomUserOnlineKey();
		key.setRoomId(entity.getRoomId());
		key.setUid(entity.getUid());
		// 清除
		// cache.clear(key);
		// 异步
		cache.clearAsync(key);
	}

	@Override
	public void batchInsert(List<RoomUserOnline> entitys, Object attach) {
		for (RoomUserOnline entity : entitys) {
			insert(entity, attach);
		}
	}

	@Override
	public RoomUserOnline selectByRoomIdAndUid(String roomId, String uid) {
		RoomUserOnlineKey key = new RoomUserOnlineKey();
		key.setRoomId(roomId);
		key.setUid(uid);
		return cache.get(key);
	}

	private String uidSql = "uid = ?";

	@Override
	public List<RoomUserOnline> selectByUid(String uid) {
		SqlQuery<RoomUserOnlineKey, RoomUserOnline> sqlQuery = new SqlQuery<>(RoomUserOnline.class, uidSql);
		sqlQuery.setArgs(uid);
		List<Entry<RoomUserOnlineKey, RoomUserOnline>> entrys = cache.query(sqlQuery).getAll();

		List<RoomUserOnline> entitys = new LinkedList<>();
		for (Entry<RoomUserOnlineKey, RoomUserOnline> entry : entrys) {
			entitys.add(entry.getValue());
		}
		return entitys;
	}

	private String roomIdSql = "roomId = ?";

	@Override
	public List<RoomUserOnline> selectByRoomId(String roomId) {
		SqlQuery<RoomUserOnlineKey, RoomUserOnline> sqlQuery = new SqlQuery<>(RoomUserOnline.class, roomIdSql);
		sqlQuery.setArgs(roomId);
		List<Entry<RoomUserOnlineKey, RoomUserOnline>> entrys = cache.query(sqlQuery).getAll();

		List<RoomUserOnline> entitys = new LinkedList<>();
		for (Entry<RoomUserOnlineKey, RoomUserOnline> entry : entrys) {
			entitys.add(entry.getValue());
		}
		return entitys;
	}
}
