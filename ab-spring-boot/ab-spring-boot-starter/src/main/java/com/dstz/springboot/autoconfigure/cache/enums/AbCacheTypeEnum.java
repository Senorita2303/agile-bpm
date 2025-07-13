package com.dstz.springboot.autoconfigure.cache.enums;

import com.dstz.springboot.autoconfigure.cache.AbMemoryCacheConfiguration;
import com.dstz.springboot.autoconfigure.cache.AbRedisCacheConfiguration;
import com.dstz.springboot.autoconfigure.cache.J2CacheConfiguration;

/**
 *
 * Cache Type
 *
 */
public enum AbCacheTypeEnum {

	/**
	 * Memory Cache
	 */
	MEMORY(AbMemoryCacheConfiguration.class),

	/**
	 * REDIS Cache
	 */
	REDIS(AbRedisCacheConfiguration.class),

	/**
	 * J2CACHE Cache
	 */
	J2CACHE(J2CacheConfiguration.class);

	private final Class<?> configClass;

	AbCacheTypeEnum(Class<?> configClass) {
		this.configClass = configClass;
	}

	public Class<?> getConfigClass() {
		return configClass;
	}
}
