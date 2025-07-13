package com.dstz.springboot.autoconfigure.cache.enums;

/**
 * j2cache first level cache provider class
 *
 */
public enum J2CacheLevel1ProviderEnum {

	/**
	 * Caffeine Cache
	 */
	CAFFEINE("caffeine"),

	/**
	 * No Cache
	 */
	NONE("none"),

	/**
	 * EhCache 2.x cache
	 */
	EHCACHE("ehcache"),

	/**
	 * EhCache 3.x cache
	 */
	EHCACHE3("ehcache3");

	private final String provider;

	J2CacheLevel1ProviderEnum(String providerClass) {
		this.provider = providerClass;
	}

	public String getProvider() {
		return provider;
	}
}
