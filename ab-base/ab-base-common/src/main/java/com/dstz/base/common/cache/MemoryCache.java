package com.dstz.base.common.cache;

import cn.hutool.core.lang.Assert;
import com.dstz.base.common.utils.CastUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Memory cache, based on Guava, with cache expiration
 *
 */
public class MemoryCache implements ICache {

	private final Map<String, Cache<String, Object>> cacheRegion;

	public MemoryCache(List<CacheRegion> cacheRegionList) {
		cacheRegion = cacheRegionList.stream().collect(Collectors.toMap(CacheRegion::getRegion, this::newCache));
	}

	private Cache<String, Object> newCache(CacheRegion cacheRegion) {
		CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
				.expireAfterAccess(cacheRegion.getExpiration());
		if (cacheRegion.getSize() != null) {
			cacheBuilder.maximumSize(cacheRegion.getSize());
		}
		return cacheBuilder.build();
	}

	private Cache<String, Object> getRegionCache(String region) {
		Cache<String, Object> cache = cacheRegion.get(region);
		Assert.notNull(cache,
				() -> new IllegalArgumentException(String.format("Cache Region %s is not configured", region)));
		return cache;
	}

	@Override
	public <V> V getIfPresent(String region, String key) {
		return CastUtils.cast(getRegionCache(region).getIfPresent(key));
	}

	@Override
	public <V> V get(String region, String key, Callable<V> loader) {
		try {
			return CastUtils.cast(getRegionCache(region).get(key, loader));
		} catch (ExecutionException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	@Override
	public void put(String region, String key, Object value) {
		// If the value is empty, it will not be put into the cache.
		if (Objects.nonNull(value)) {
			getRegionCache(region).put(key, value);
		}
	}

	@Override
	public void invalidate(String region, String... keys) {
		getRegionCache(region).invalidateAll(Arrays.asList(keys));
	}

	@Override
	public void invalidateRegion(String region) {
		getRegionCache(region).invalidateAll();
	}

	@Override
	public void invalidateAll() {
		cacheRegion.values().forEach(Cache::invalidateAll);
	}

	@Override
	public boolean exists(String region, String key) {
		return Objects.nonNull(getRegionCache(region).getIfPresent(key));
	}
}
