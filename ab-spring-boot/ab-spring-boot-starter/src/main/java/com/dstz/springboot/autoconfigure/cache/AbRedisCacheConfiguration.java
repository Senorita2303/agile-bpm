package com.dstz.springboot.autoconfigure.cache;

import com.dstz.base.common.cache.ICache;
import com.dstz.component.redis.cache.AbRedisCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * ab redis cache
 *
 */
@Conditional(AbCacheConditional.class)
@Configuration
public class AbRedisCacheConfiguration {

	private final AbCacheProperties abCacheProperties;

	public AbRedisCacheConfiguration(AbCacheProperties abCacheProperties) {
		this.abCacheProperties = abCacheProperties;
	}

	@Bean
	public ICache redisCache(RedisConnectionFactory redisConnectionFactory) {
		return new AbRedisCache(abCacheProperties.getCacheRegionList(), redisConnectionFactory);
	}
}
