package com.dstz.springboot.autoconfigure.cache;

import com.dstz.base.common.cache.AbSpringCacheManager;
import com.dstz.base.common.cache.ICache;
import com.dstz.springboot.autoconfigure.cache.enums.AbCacheTypeEnum;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Cache Auto-Configuration
 *
 */
@EnableConfigurationProperties(AbCacheProperties.class)
@Import(AbCacheConfiguration.AbCacheConfigurationSelector.class)
@EnableCaching
@Configuration
public class AbCacheConfiguration {


    public static class AbCacheConfigurationSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            AbCacheTypeEnum[] types = AbCacheTypeEnum.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = types[i].getConfigClass().getName();
            }
            return imports;
        }
    }

    @Bean
    public CacheManager cacheManager(ICache cache) {
        return new AbSpringCacheManager(cache);
    }
}
