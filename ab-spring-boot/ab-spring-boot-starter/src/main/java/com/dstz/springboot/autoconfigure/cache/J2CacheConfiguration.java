package com.dstz.springboot.autoconfigure.cache;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.cache.CacheRegion;
import com.dstz.base.common.constats.StrPool;
import com.dstz.component.j2cache.AbJ2Cache;
import com.dstz.component.j2cache.J2CacheChannelFactoryBean;
import com.dstz.component.j2cache.constant.J2CacheBroadcastPropertyKeyConstant;
import com.dstz.component.j2cache.constant.J2CacheL2PropertyKeyConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * j2cache configuration
 *
 */
@Conditional(AbCacheConditional.class)
@EnableConfigurationProperties(J2CacheProperties.class)
public class J2CacheConfiguration {

	private final AbCacheProperties abCacheProperties;

	private final J2CacheProperties j2CacheProperties;

	public J2CacheConfiguration(AbCacheProperties abCacheProperties, J2CacheProperties j2CacheProperties) {
		this.abCacheProperties = abCacheProperties;
		this.j2CacheProperties = j2CacheProperties;
	}

	@Bean
	public AbJ2Cache abJ2Cache() {
		return new AbJ2Cache();
	}

	@Bean
	public J2CacheChannelFactoryBean j2CacheChannelFactoryBean() {
		// L1 Cache property configuration
		Properties l1Properties = new Properties();
		if (j2CacheProperties.getLevel1().getProperties() != null) {
			l1Properties.putAll(j2CacheProperties.getLevel1().getProperties());
		}

		// L2 Cache property configuration
		Properties l2Properties = new Properties();
		if (j2CacheProperties.getLevel2().getProperties() != null) {
			l2Properties.putAll(j2CacheProperties.getLevel2().getProperties());
		}

		// Build cache format
		List<CacheRegion> cacheRegionList = abCacheProperties.getCacheRegionList();
		for (CacheRegion cacheRegion : cacheRegionList) {
			String regionName = StrUtil.concat(true, "region.", cacheRegion.getRegion());
			String regionExpire = StrUtil.concat(true,
					Long.toString(ObjectUtil.defaultIfNull(cacheRegion.getSize(), Long.MAX_VALUE)), StrPool.COMMA,
					Long.toString(cacheRegion.getExpiration().getSeconds()), "s");
			l1Properties.put(regionName, regionExpire);
			l2Properties.put(regionName, regionExpire);
		}
		// The cache area is placed in the configuration file for other implementations to read
		l1Properties.put(CacheRegion.class, cacheRegionList);
		l2Properties.put(CacheRegion.class, cacheRegionList);
		l2Properties.setProperty(J2CacheL2PropertyKeyConstant.CACHE_OPEN,
				Objects.toString(Boolean.TRUE.equals(j2CacheProperties.getLevel2().getCacheOpen())));

		// Broadcast properties
		Properties broadcastProperties = new Properties();
		if (j2CacheProperties.getBroadcast().getProperties() != null) {
			broadcastProperties.putAll(j2CacheProperties.getBroadcast().getProperties());
		}
		// Is it enabled?
		broadcastProperties.put(J2CacheBroadcastPropertyKeyConstant.OPEN,
				Boolean.toString(Boolean.TRUE.equals(j2CacheProperties.getBroadcast().getOpen())));
		broadcastProperties.put(J2CacheBroadcastPropertyKeyConstant.CHANNEL,
				j2CacheProperties.getBroadcast().getChannel());

		J2CacheChannelFactoryBean factoryBean = new J2CacheChannelFactoryBean();
		factoryBean.setSerialization(
				j2CacheProperties.getSerializationClass() == null ? j2CacheProperties.getSerialization().getSerializer()
						: j2CacheProperties.getSerializationClass().getName());
		factoryBean.setBroadcastProviderClass(j2CacheProperties.getBroadcast().getProviderClass() == null
				? j2CacheProperties.getBroadcast().getProvider().getProvider()
				: j2CacheProperties.getBroadcast().getProviderClass().getName());
		factoryBean.setCacheL1ProviderClass(j2CacheProperties.getLevel1().getProviderClass() == null
				? j2CacheProperties.getLevel1().getProvider().getProvider()
				: j2CacheProperties.getLevel1().getProviderClass().getName());
		factoryBean.setCacheL2ProviderClass(j2CacheProperties.getLevel2().getProviderClass() == null
				? j2CacheProperties.getLevel2().getProvider().getProvider()
				: j2CacheProperties.getLevel2().getProviderClass().getName());
		factoryBean.setDefaultCacheNullObject(Boolean.TRUE.equals(j2CacheProperties.getDefaultCacheNullObject()));
		factoryBean.setCacheL1Properties(l1Properties);
		factoryBean.setCacheL2Properties(l2Properties);
		factoryBean.setBroadcastProperties(broadcastProperties);
		return factoryBean;
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
		ThreadPoolTaskExecutor taskExecutor = new TaskExecutorBuilder().allowCoreThreadTimeOut(true)
				.awaitTermination(true).corePoolSize(2).keepAlive(Duration.ofSeconds(60))
				.threadNamePrefix(RedisMessageListenerContainer.DEFAULT_THREAD_NAME_PREFIX)
				.maxPoolSize(Math.max(Math.round(Runtime.getRuntime().availableProcessors() * (float) 0.2), 2)).build();
		taskExecutor.initialize();

		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
		redisMessageListenerContainer.setTaskExecutor(taskExecutor);
		return redisMessageListenerContainer;
	}

}
