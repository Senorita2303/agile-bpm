package com.dstz.base.common.cache;

import cn.hutool.core.util.StrUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ab spring cache manager
 *
 */
public class AbSpringCacheManager implements CacheManager {

	private final ConcurrentHashMap<String, AbSpringCache> regionCacheMap = new ConcurrentHashMap<>();

	private final ICache abCache;

	public AbSpringCacheManager(ICache abCache) {
		this.abCache = abCache;
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = regionCacheMap.computeIfAbsent(StrUtil.nullToEmpty(name),
				region -> new AbSpringCache(abCache, region));
		return new TransactionAwareCacheDecorator(cache);
	}

	@Override
	public Collection<String> getCacheNames() {
		return regionCacheMap.keySet();
	}

}
