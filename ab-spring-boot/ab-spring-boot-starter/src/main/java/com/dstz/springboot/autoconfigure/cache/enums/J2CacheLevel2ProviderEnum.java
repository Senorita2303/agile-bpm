package com.dstz.springboot.autoconfigure.cache.enums;

import com.dstz.component.j2cache.redis.SpringRedisCacheProvider;

/**
 * j2cache first level cache provider class
 *
 */
public enum J2CacheLevel2ProviderEnum {

	/**
	 * No Cache
	 */
	NONE("none"),

	/**
	 * REDIS
	 */
	REDIS(SpringRedisCacheProvider.class.getName());

	private final String provider;

	J2CacheLevel2ProviderEnum(String providerClass) {
		this.provider = providerClass;
	}

	public String getProvider() {
		return provider;
	}
}
