package com.dstz.springboot.autoconfigure.cache.enums;

import com.dstz.component.j2cache.redis.RedisPubSubClusterPolicy;

/**
 * j2cache broadcast
 *
 */
public enum J2CacheBroadcastEnum {

	/**
	 * Redis publish and subscribe
	 */
	REDIS(RedisPubSubClusterPolicy.class.getName()),

	/**
	 * Empty Notification
	 */
	NONE("none"),

	/**
	 * Multicast
	 */
	JGROUPS("jgroups");

	private final String provider;

	J2CacheBroadcastEnum(String provider) {
		this.provider = provider;
	}

	public String getProvider() {
		return provider;
	}
}
