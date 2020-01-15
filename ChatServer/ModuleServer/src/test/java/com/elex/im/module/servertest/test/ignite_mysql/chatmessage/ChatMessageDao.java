package com.elex.im.module.servertest.test.ignite_mysql.chatmessage;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.cache.Cache.Entry;
import javax.cache.configuration.FactoryBuilder;

import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheKeyConfiguration;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.QueryEntity;
import org.apache.ignite.cache.QueryIndex;
import org.apache.ignite.cache.QueryIndexType;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.elex.common.component.data.ADataDao;
import com.elex.common.component.ignite.IIgniteService;

public class ChatMessageDao extends ADataDao<ChatMessage> implements IChatMessageDao {
	private IgniteCache<ChatMessageKey, ChatMessage> cache;

	private ConcurrentMap<String, IgniteAtomicLong> idCreaters;

	public ChatMessageDao(IIgniteService igniteService) {
		super(igniteService);
		this.idCreaters = new ConcurrentHashMap<>();
		init();
	}

	private void init() {
		// Create near-cache configuration for "myCache".
		// NearCacheConfiguration<ChatMessageKey, ChatMessage> nearCfg = new
		// NearCacheConfiguration<>();
		// Use LRU eviction policy to automatically evict entries
		// from near-cache, whenever it reaches 100_000 in size.
		// nearCfg.setNearEvictionPolicy(new LruEvictionPolicy<>(100_000));

		CacheConfiguration<ChatMessageKey, ChatMessage> cfg = new CacheConfiguration<>();
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
		// cfg.setIndexedTypes(ChatMessageKey.class, ChatMessage.class);

		CacheKeyConfiguration cacheKeyConfiguration = new CacheKeyConfiguration(getKeyClass());
		cacheKeyConfiguration.setAffinityKeyFieldName("roomId");
		cfg.setKeyConfiguration(cacheKeyConfiguration);

		// 设置数据分区设置
		// cfg.setDataRegionName(getPojoClass().getSimpleName());

		// 数据库配置
		setJDBCStore(cfg);

		// 添加过滤
		cfg.setNodeFilter(new ChatMessageNodeFilter());

		// Create cache with given name, if it does not exist.
		// 异步支持,原子操作,EntryProcessor
		cache = igniteService.getIgnite().getOrCreateCache(cfg);
	}

	public static DataRegionConfiguration createDataRegionConfiguration(String swapDirectory) {
		// Creating a new data region.
		DataRegionConfiguration regionCfg = new DataRegionConfiguration();
		regionCfg.setPersistenceEnabled(true);
		regionCfg.setName(ChatMessage.class.getSimpleName());
		// Setting initial RAM size. 100M
		regionCfg.setInitialSize(100L * 1024 * 1024);
		// Setting maximum RAM size. 500M
		regionCfg.setMaxSize(500L * 1024 * 1024);
		// Enable persistence for the region.
		regionCfg.setPersistenceEnabled(true);

		regionCfg.setSwapPath(swapDirectory + File.separator + ChatMessage.class.getSimpleName());
		return regionCfg;
	}

	private void setJDBCStore(CacheConfiguration<ChatMessageKey, ChatMessage> cfg) {
		// Configure JDBC store.
		cfg.setCacheStoreFactory(FactoryBuilder.factoryOf(ChatMessageCacheJdbcStore.class));
		// Configure JDBC session listener.
		cfg.setCacheStoreSessionListenerFactories(new MysqlStoreListenerFactory());
		cfg.setReadThrough(true);
		cfg.setWriteThrough(true);
		cfg.setWriteBehindEnabled(true);

		cfg.setWriteBehindFlushThreadCount(10);// 默认1
	}

	private QueryEntity createQueryEntity() {
		QueryEntity queryEntity = new QueryEntity();

		// key-value类型
		queryEntity.setKeyType(getKeyClass().getName());
		queryEntity.setValueType(getPojoClass().getName());

		// 域类型
		LinkedHashMap<String, String> fields = new LinkedHashMap<>();
		fields.put("roomId", String.class.getName());
		fields.put("orderId", Long.class.getName());
		fields.put("uid", String.class.getName());
		fields.put("contentType", Integer.class.getName());
		fields.put("content", String.class.getName());// 内容可以设置全文检索
		fields.put("atUids", String.class.getName());
		fields.put("sendedTime", Long.class.getName());
		fields.put("receivedTime", Long.class.getName());
		fields.put("clientExt", String.class.getName());
		fields.put("serverExt", String.class.getName());

		queryEntity.setFields(fields);

		// 索引
		LinkedHashMap<String, Boolean> indexs = new LinkedHashMap<String, Boolean>();
		indexs.put("roomId", true);
		indexs.put("orderId", false);
		QueryIndex groupIndex = new QueryIndex(indexs, QueryIndexType.SORTED);
		// QueryIndex roomIdIndex = new QueryIndex("roomId");
		// QueryIndex orderIdIndex = new QueryIndex("orderId");// desc
		queryEntity.setIndexes(Arrays.asList(groupIndex));

		// 需要全文检索需要设置索引
		// queryEntity.setIndexes(Arrays.asList(new QueryIndex("content",
		// QueryIndexType.FULLTEXT),
		// new QueryIndex("name", QueryIndexType.FULLTEXT),
		// new QueryIndex("addr.street", QueryIndexType.FULLTEXT)));
		return queryEntity;
	}

	@Override
	public Class<ChatMessage> getPojoClass() {
		return ChatMessage.class;
	}

	private Class<ChatMessageKey> getKeyClass() {
		return ChatMessageKey.class;
	}

	@Override
	public void insert(ChatMessage entity, Object attach) {
		ChatMessageKey key = new ChatMessageKey();
		key.setOrderId(entity.getOrderId());
		key.setRoomId(entity.getRoomId());

		// 这里做类型转换
		entity.saveBefore();
		// 异步
		cache.putAsync(key, entity);
		// cache.put(key, entity);
	}

	@Override
	public void batchInsert(List<ChatMessage> entitys, Object attach) {
		for (ChatMessage entity : entitys) {
			insert(entity, attach);
		}
	}

	@Override
	public void update(ChatMessage entity, Object attach) {
		insert(entity, attach);
	}

	@Override
	public ChatMessage selectByRoomIdAndOrder(String roomId, long orderId) {
		ChatMessageKey key = new ChatMessageKey();
		key.setOrderId(orderId);
		key.setRoomId(roomId);

		ChatMessage entity = cache.get(key);
		if (entity != null) {
			// 类型转换
			entity.obtainAfter();
		}
		return entity;
	}

	private String maxOrderSql = "select max(`orderId`) from ChatMessage where roomId=?";

	private long getMaxOrder2(String roomId) {
		SqlFieldsQuery sqlQuery = new SqlFieldsQuery(maxOrderSql);
		sqlQuery.setArgs(roomId);
		QueryCursor<List<?>> cursor = cache.query(sqlQuery);
		long maxOrder = 0;
		for (List<?> row : cursor.getAll()) {
			Object obj = row.get(0);
			if (obj != null) {
				maxOrder = (long) obj;
			}
			break;
		}
		return maxOrder;
	}

	@Override
	public long getMaxOrder(String roomId) {
		long order = 0;
		int repeated = 0;
		for (;;) {
			if (repeated > 2) {
				throw new RuntimeException("Fatal error! IgniteAtomicLong is Error!!!");
			}
			IgniteAtomicLong seq = getIdCreater(roomId);
			try {
				order = seq.get();
				break;
			} catch (Exception e) {
				logger.error("", e);
				idCreaters.remove(roomId);
			}
			repeated++;
		}
		return order;
	}

	@Override
	public long getNextOrder(String roomId) {
		long order = 0;
		int repeated = 0;
		for (;;) {
			if (repeated > 2) {
				throw new RuntimeException("Fatal error! IgniteAtomicLong is Error!!!");
			}
			IgniteAtomicLong seq = getIdCreater(roomId);
			try {
				order = seq.incrementAndGet();
				break;
			} catch (Exception e) {
				logger.error("", e);
				idCreaters.remove(roomId);
			}
			repeated++;
		}
		return order;
	}

	private String MaxOrderKey_Template = "MaxOrder_%s";

	private IgniteAtomicLong getIdCreater(String roomId) {
		IgniteAtomicLong seq = idCreaters.get(roomId);
		if (seq != null) {
			return seq;
		}
		seq = createIgniteAtomicLong(roomId);

		IgniteAtomicLong tseq = idCreaters.putIfAbsent(roomId, seq);
		if (tseq == null) {
			tseq = seq;
		}
		return tseq;
	}

	private IgniteAtomicLong createIgniteAtomicLong(String roomId) {
		String key = String.format(MaxOrderKey_Template, roomId);
		long max = getMaxOrder2(roomId);
		IgniteAtomicLong seq = igniteService.getIgnite().atomicLong(key, max, true);
		return seq;
	}

	private String ridAndCountSql = "roomId=? order by `orderId` desc limit ?";

	@Override
	public List<ChatMessage> selectByRoomIdAndCount(String roomId, int newMaxCount) {
		SqlQuery<ChatMessageKey, ChatMessage> sqlQuery = new SqlQuery<>(ChatMessage.class, ridAndCountSql);
		sqlQuery.setArgs(roomId, newMaxCount);
		QueryCursor<Entry<ChatMessageKey, ChatMessage>> cursor = cache.query(sqlQuery);

		List<ChatMessage> entitys = new LinkedList<>();

		for (Entry<ChatMessageKey, ChatMessage> entry : cursor.getAll()) {
			ChatMessage entity = entry.getValue();
			// 类型转换
			entity.obtainAfter();
			entitys.add(entity);
		}
		return entitys;
	}

	@Override
	public List<ChatMessage> selectByRoomIdAndOrders(String roomId, List<Long> orders) {
		List<ChatMessage> list = new LinkedList<>();
		for (long order : orders) {
			ChatMessage entity = selectByRoomIdAndOrder(roomId, order);
			if (entity != null) {
				list.add(entity);
			}
		}
		return list;
	}

	@Override
	public List<ChatMessage> selectByRoomIdAndOrderPage(String roomId, long orderId, int pageCount) {
		List<ChatMessage> list = new LinkedList<>();
		for (int i = 0; i < pageCount; i++) {
			ChatMessage entity = selectByRoomIdAndOrder(roomId, orderId + i);
			if (entity != null) {
				list.add(entity);
			}
		}
		return list;
	}
}
