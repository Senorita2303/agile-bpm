package com.dstz.base.common.cache;

import java.util.concurrent.Callable;

/**
 * <pre>
 * System Cache
 * </pre>
 */
public interface ICache {

	/**
	 * Get the cache, if it does not exist, return null
	 *
	 * @param region Cache area
	 * @param key    Cache Key
	 * @return Caching Values
	 */
	<V> V getIfPresent(String region, String key);

	/**
	 * Get the cache. If the cache does not exist, load it from the loader.
	 *
	 * @param region Cache area
	 * @param key    Cache Key
	 * @param loader Cache Loader
	 * @return Caching Values
	 */
	<V> V get(String region, String key, Callable<V> loader);

	/**
	 * Place the cache
	 *
	 * @param region Cache area
	 * @param key    Cache Key
	 * @param value  Caching Values
	 */
	void put(String region, String key, Object value);

	/**
	 * Cache Invalidation
	 *
	 * @param region Cache area
	 * @param key    Cache Key
	 */
	void invalidate(String region, String... key);

	/**
	 * Cache region invalidation
	 *
	 * @param region Cache area
	 */
	void invalidateRegion(String region);

	/**
	 * Invalidate all caches
	 */
	void invalidateAll();

	/**
	 * Whether the cache exists
	 *
	 * @param region Cache area
	 * @param key    Cache Key
	 * @return Does it exist?
	 */
	boolean exists(String region, String key);
}
