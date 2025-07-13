package com.dstz.component.j2cache.redis;

import cn.hutool.core.util.BooleanUtil;
import com.dstz.base.common.cache.CacheRegion;
import com.dstz.component.j2cache.constant.J2CacheL2PropertyKeyConstant;
import net.oschina.j2cache.*;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ab redis cache cache provider, integrated with spring
 *
 */
public class SpringRedisCacheProvider implements CacheProvider {

    private final ConcurrentHashMap<String, SpringRedisCache> regionCacheMap = new ConcurrentHashMap<>();

    private boolean cacheOpen;

    private Level2Cache nullCache;

    @Override
    public String name() {
        return "redis";
    }


    @Override
    public int level() {
        return CacheObject.LEVEL_2;
    }

    @Override
    public Cache buildCache(String regionName, CacheExpiredListener listener) {
        return buildCache(regionName, Long.MAX_VALUE, listener);
    }

    @Override
    public Cache buildCache(String regionName, long timeToLiveInSeconds, CacheExpiredListener listener) {
        if (cacheOpen) {
            return regionCacheMap.get(regionName);
        } else {
            return nullCache;
        }
    }

    @Override
    public Collection<CacheChannel.Region> regions() {
        return regionCacheMap.values().stream().map(item -> new CacheChannel.Region(item.getRegion(), Long.MAX_VALUE, item.getTimeout().getSeconds())).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void start(Properties props) {
        this.cacheOpen = BooleanUtil.toBoolean(props.getProperty(J2CacheL2PropertyKeyConstant.CACHE_OPEN));
        if (this.cacheOpen) {
            final RedisTemplate<String, Object> redisTemplate = RedisTemplateUtils.createRedisTemplate();
            final String namespace = props.getProperty(J2CacheL2PropertyKeyConstant.NAMESPACE);
            List<CacheRegion> cacheRegionList = (List<CacheRegion>) props.get(CacheRegion.class);
            this.regionCacheMap.clear();
            for (CacheRegion cacheRegion : cacheRegionList) {
                this.regionCacheMap.put(cacheRegion.getRegion(), new SpringRedisCache(namespace, cacheRegion.getRegion(), cacheRegion.getExpiration(), redisTemplate));
            }
        } else {
            this.nullCache = new NullCache();
        }
    }

    @Override
    public void stop() {
        this.regionCacheMap.clear();
    }
}
