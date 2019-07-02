package com.elex.common.component.member.data;

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

public class MemberOnlineDao extends ADataDao<MemberOnline> implements IMemberOnlineDao {
	private IgniteCache<MemberOnlineKey, MemberOnline> cache;

	public MemberOnlineDao(IIgniteService igniteService) {
		super(igniteService);
		init();
	}

	private void init() {
		// Create near-cache configuration for "myCache".
		// NearCacheConfiguration<MemberOnlineKey, MemberOnline> nearCfg = new
		// NearCacheConfiguration<>();
		// Use LRU eviction policy to automatically evict entries
		// from near-cache, whenever it reaches 100_000 in size.
		// nearCfg.setNearEvictionPolicy(new LruEvictionPolicy<>(100_000));

		CacheConfiguration<MemberOnlineKey, MemberOnline> cfg = new CacheConfiguration<>();
		cfg.setName(getPojoClass().getSimpleName().toUpperCase());
		// cfg.setGroupName(MemberOnline.class.getSimpleName());
		// 分区模式 LOCAL, REPLICATED, PARTITIONED
		cfg.setCacheMode(CacheMode.REPLICATED);// 设置有每个服务器都有完整备份
		// TRANSACTIONAL, ATOMIC原子有序写模式
		cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		// cfg.setAtomicWriteOrderMode(CacheAtomicWriteOrderMode.CLOCK);
		cfg.setBackups(1);
		// FULL_SYNC, FULL_ASYNC, PRIMARY_SYNC
		cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
		// cfg.setNearConfiguration(nearCfg);// 近缓存
		cfg.setCopyOnRead(false);
		cfg.setOnheapCacheEnabled(true);
		// cfg.setInvalidate(true);// Invalidate near cache

		cfg.setQueryEntities(Arrays.asList(createQueryEntity()));

		CacheKeyConfiguration cacheKeyConfiguration = new CacheKeyConfiguration(getKeyClass());
		cacheKeyConfiguration.setAffinityKeyFieldName("memberId");
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
		fields.put("memberId", String.class.getName());
		fields.put("memberType", String.class.getName());
		fields.put("functionType", String.class.getName());
		fields.put("sid", String.class.getName());
		fields.put("loginTime", Long.class.getName());
		fields.put("extend", String.class.getName());

		queryEntity.setFields(fields);

		// 索引
		// QueryIndex memberIdIndex = new QueryIndex("memberId");
		QueryIndex memberTypeIndex = new QueryIndex("memberType");
		QueryIndex functionTypeIndex = new QueryIndex("functionType");
		QueryIndex sidIndex = new QueryIndex("sid");
		queryEntity.setIndexes(Arrays.asList(sidIndex, memberTypeIndex, functionTypeIndex));

		return queryEntity;
	}

	public static DataRegionConfiguration createDataRegionConfiguration(String swapDirectory) {
		DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
		dataRegionConfiguration.setPersistenceEnabled(true);
		dataRegionConfiguration.setName(MemberOnline.class.getSimpleName());
		dataRegionConfiguration.setSwapPath(swapDirectory + File.separator + MemberOnline.class.getSimpleName());
		return dataRegionConfiguration;
	}

	@Override
	public Class<MemberOnline> getPojoClass() {
		return MemberOnline.class;
	}

	private Class<MemberOnlineKey> getKeyClass() {
		return MemberOnlineKey.class;
	}

	@Override
	public void insert(MemberOnline entity, Object attach) {
		logger.debug("Dao MemberOnline insert!!!");

		MemberOnlineKey key = new MemberOnlineKey();
		key.setFunctionType(entity.getFunctionType());
		key.setMemberId(entity.getMemberId());
		// 插入
		// cache.put(key, entity);
		// 异步
		cache.putAsync(key, entity);
	}

	@Override
	public void remove(MemberOnline entity, Object attach) {
		MemberOnlineKey key = new MemberOnlineKey();
		key.setFunctionType(entity.getFunctionType());
		key.setMemberId(entity.getMemberId());
		// 清除
		// cache.clear(key);
		cache.clearAsync(key);
	}

	@Override
	public void batchInsert(List<MemberOnline> entitys, Object attach) {
		for (MemberOnline entity : entitys) {
			insert(entity, attach);
		}
	}

	@Override
	public void batchRemove(List<MemberOnline> entitys, Object attach) {
		for (MemberOnline entity : entitys) {
			remove(entity, attach);
		}
	}

	@Override
	public MemberOnline selectByMemberIdAndFunctionType(String memberId, String functionType) {
		MemberOnlineKey key = new MemberOnlineKey();
		key.setMemberId(memberId);
		key.setFunctionType(functionType);
		return cache.get(key);
	}

	// private String sidSql = "sid = ?";

	// @Override
	// public List<MemberOnline> selectBySid(String sid) {
	// SqlQuery<MemberOnlineKey, MemberOnline> sqlQuery = new
	// SqlQuery<>(MemberOnline.class, sidSql);
	// sqlQuery.setArgs(sid);
	// List<Entry<MemberOnlineKey, MemberOnline>> entrys =
	// cache.query(sqlQuery).getAll();
	//
	// List<MemberOnline> entitys = new LinkedList<>();
	// for (Entry<MemberOnlineKey, MemberOnline> entry : entrys) {
	// entitys.add(entry.getValue());
	// }
	// return entitys;
	// }

	// private String memberIdSql = "memberId = ?";

	// @Override
	// public List<MemberOnline> selectByMemberId(String memberId) {
	// SqlQuery<MemberOnlineKey, MemberOnline> sqlQuery = new
	// SqlQuery<>(MemberOnline.class, memberIdSql);
	// sqlQuery.setArgs(memberId);
	// List<Entry<MemberOnlineKey, MemberOnline>> entrys =
	// cache.query(sqlQuery).getAll();
	//
	// List<MemberOnline> entitys = new LinkedList<>();
	// for (Entry<MemberOnlineKey, MemberOnline> entry : entrys) {
	// entitys.add(entry.getValue());
	// }
	// return entitys;
	// }

	private String sidAndMemberTypeSql = "sid = ? and memberType = ? and functionType = ?";

	@Override
	public List<MemberOnline> selectBySidAndMemberTypeAndFunctionType(String sid, String memberType, String functionType) {
		SqlQuery<MemberOnlineKey, MemberOnline> sqlQuery = new SqlQuery<>(MemberOnline.class, sidAndMemberTypeSql);
		sqlQuery.setArgs(sid, memberType, functionType);
		List<Entry<MemberOnlineKey, MemberOnline>> entrys = cache.query(sqlQuery).getAll();

		List<MemberOnline> entitys = new LinkedList<>();
		for (Entry<MemberOnlineKey, MemberOnline> entry : entrys) {
			entitys.add(entry.getValue());
		}
		return entitys;
	}
}
