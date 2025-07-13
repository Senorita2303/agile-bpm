package com.dstz.base.common.cache;

import java.util.concurrent.Callable;

import cn.hutool.extra.spring.SpringUtil;

/**
 * <pre>
 * Tools for convenient use of partition cache
 * </pre>
 * @param <T>
 */
public class RegionCacheUtil<T> {
	private String region;

	public RegionCacheUtil(String region) {
		super();
		this.region = region;
	}

	public void put(String key, T obj) {
		SpringUtil.getBean(ICache.class).put(region, key, obj);
	}

	public T get(String key) {
		return (T) SpringUtil.getBean(ICache.class).getIfPresent(region, key);
	}

	public T get(String key, Callable<T> loader) {
		return (T) SpringUtil.getBean(ICache.class).get(region, key, loader);
	}

	public void remove(String key) {
		SpringUtil.getBean(ICache.class).invalidate(region, key);
	}

	public void removeAll() {
		SpringUtil.getBean(ICache.class).invalidateRegion(region);
	}
}
