package com.dstz.springboot.autoconfigure.cache;

import com.dstz.springboot.autoconfigure.cache.enums.J2CacheBroadcastEnum;
import com.dstz.springboot.autoconfigure.cache.enums.J2CacheLevel1ProviderEnum;
import com.dstz.springboot.autoconfigure.cache.enums.J2CacheLevel2ProviderEnum;
import com.dstz.springboot.autoconfigure.cache.enums.J2CacheSerializationEnum;
import net.oschina.j2cache.CacheProvider;
import net.oschina.j2cache.cluster.ClusterPolicy;
import net.oschina.j2cache.util.Serializer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * J2Cache property configuration
 *
 */
@ConfigurationProperties(prefix = "j2cache")
public class J2CacheProperties {

	/**
	 * Serialization
	 */
	private J2CacheSerializationEnum serialization = J2CacheSerializationEnum.JSON;

	/**
	 * Custom serialization implementation class
	 */
	private Class<? extends Serializer> serializationClass;

	/**
	 * Whether to cache empty objects
	 */
	private Boolean defaultCacheNullObject = Boolean.FALSE;

	/**
	 * Level 1 Cache
	 */
	private final Level1CacheProperties level1 = new Level1CacheProperties();

	/**
	 * Second level cache
	 */
	private final Level2CacheProperties level2 = new Level2CacheProperties();

	/**
	 * broadcast
	 */
	private final BroadcastProperties broadcast = new BroadcastProperties();

	public J2CacheSerializationEnum getSerialization() {
		return serialization;
	}

	public void setSerialization(J2CacheSerializationEnum serialization) {
		this.serialization = serialization;
	}

	public Class<? extends Serializer> getSerializationClass() {
		return serializationClass;
	}

	public void setSerializationClass(Class<? extends Serializer> serializationClass) {
		this.serializationClass = serializationClass;
	}

	public Boolean getDefaultCacheNullObject() {
		return defaultCacheNullObject;
	}

	public void setDefaultCacheNullObject(Boolean defaultCacheNullObject) {
		this.defaultCacheNullObject = defaultCacheNullObject;
	}

	public Level1CacheProperties getLevel1() {
		return level1;
	}

	public Level2CacheProperties getLevel2() {
		return level2;
	}

	public BroadcastProperties getBroadcast() {
		return broadcast;
	}

	/**
	 * Second level cache
	 */
	public static class Level1CacheProperties {

		/**
		 * Second level cache
		 */
		private J2CacheLevel1ProviderEnum provider = J2CacheLevel1ProviderEnum.CAFFEINE;

		/**
		 * Second level cache implementation provides class
		 */
		private Class<? extends CacheProvider> providerClass;

		/**
		 * property
		 */
		private Map<String, String> properties;

		public J2CacheLevel1ProviderEnum getProvider() {
			return provider;
		}

		public void setProvider(J2CacheLevel1ProviderEnum provider) {
			this.provider = provider;
		}

		public Class<? extends CacheProvider> getProviderClass() {
			return providerClass;
		}

		public void setProviderClass(Class<? extends CacheProvider> providerClass) {
			this.providerClass = providerClass;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}
	}

	/**
	 * Second level cache
	 */
	public static class Level2CacheProperties {

		/**
		 * Enable L2 cache
		 */
		private Boolean cacheOpen = Boolean.FALSE;

		/**
		 * Second level cache
		 */
		private J2CacheLevel2ProviderEnum provider = J2CacheLevel2ProviderEnum.REDIS;

		/**
		 * Second level cache implementation provides class
		 */
		private Class<? extends CacheProvider> providerClass;

		/**
		 * property
		 */
		private Map<String, String> properties;

		public Boolean getCacheOpen() {
			return cacheOpen;
		}

		public void setCacheOpen(Boolean cacheOpen) {
			this.cacheOpen = cacheOpen;
		}

		public J2CacheLevel2ProviderEnum getProvider() {
			return provider;
		}

		public void setProvider(J2CacheLevel2ProviderEnum provider) {
			this.provider = provider;
		}

		public Class<? extends CacheProvider> getProviderClass() {
			return providerClass;
		}

		public void setProviderClass(Class<? extends CacheProvider> providerClass) {
			this.providerClass = providerClass;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}
	}

	/**
	 * Broadcast properties
	 */
	public static class BroadcastProperties {

		/**
		 * Start Broadcast
		 */
		private Boolean open = Boolean.FALSE;

		/**
		 * Broadcast Implementation
		 */
		private J2CacheBroadcastEnum provider = J2CacheBroadcastEnum.REDIS;

		/**
		 * Broadcast implementation class
		 */
		private Class<? extends ClusterPolicy> providerClass;

		/**
		 * Broadcast channel name
		 */
		private String channel = "j2cache";

		/**
		 * property
		 */
		private Map<String, String> properties;

		public Boolean getOpen() {
			return open;
		}

		public void setOpen(Boolean open) {
			this.open = open;
		}

		public J2CacheBroadcastEnum getProvider() {
			return provider;
		}

		public void setProvider(J2CacheBroadcastEnum provider) {
			this.provider = provider;
		}

		public Class<? extends ClusterPolicy> getProviderClass() {
			return providerClass;
		}

		public void setProviderClass(Class<? extends ClusterPolicy> providerClass) {
			this.providerClass = providerClass;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public void setProperties(Map<String, String> properties) {
			this.properties = properties;
		}
	}

}
