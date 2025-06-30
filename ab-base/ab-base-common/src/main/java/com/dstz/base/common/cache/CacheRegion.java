package com.dstz.base.common.cache;

import java.time.Duration;

/**
 * Cache area
 *
 */
public class CacheRegion {

	/**
	 * area
	 */
	private String region;

	/**
	 * Expiration time
	 */
	private Duration expiration;

	/**
	 * Cache size
	 */
	private Long size;

	public CacheRegion(String region, Duration expiration) {
		this(region, expiration, null);
	}

	public CacheRegion(String region, Duration expiration, Long size) {
		this.region = region;
		this.expiration = expiration;
		this.size = size;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Duration getExpiration() {
		return expiration;
	}

	public void setExpiration(Duration expiration) {
		this.expiration = expiration;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "CacheRegion{" + "region='" + region + '\'' + ", expiration=" + expiration + '}';
	}
}
