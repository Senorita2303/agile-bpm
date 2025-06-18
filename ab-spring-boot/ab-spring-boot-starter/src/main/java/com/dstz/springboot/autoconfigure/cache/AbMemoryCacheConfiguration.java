package com.dstz.springboot.autoconfigure.cache;

import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.cache.MemoryCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * Memory cache configuration
 *
 */
@Conditional(AbCacheConditional.class)
public class AbMemoryCacheConfiguration {

	private final AbCacheProperties abCacheProperties;

	public AbMemoryCacheConfiguration(AbCacheProperties abCacheProperties) {
		this.abCacheProperties = abCacheProperties;
	}

	@Bean
	public ICache memoryCache() {
		return new MemoryCache(abCacheProperties.getCacheRegionList());
	}
}
