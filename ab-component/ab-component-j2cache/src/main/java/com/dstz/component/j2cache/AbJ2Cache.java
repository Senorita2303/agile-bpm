package com.dstz.component.j2cache;

import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.utils.CastUtils;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import java.util.concurrent.Callable;

/**
 * j2cache cache implementation
 *
 */
public class AbJ2Cache implements ICache {

    @Autowired
    private CacheChannel cacheChannel;

    @Override
    public <V> V getIfPresent(String region, String key) {
        return CastUtils.cast(cacheChannel.get(region, key).getValue());
    }

    @Override
    public <V> V get(String region, String key, Callable<V> loader) {
        Object value = cacheChannel.get(region, key, k -> {
            try {
                return loader.call();
            } catch (Exception e) {
                ReflectionUtils.rethrowRuntimeException(e);
                return null;
            }
        }).getValue();
        return CastUtils.cast(value);
    }

    @Override
    public void put(String region, String key, Object value) {
        cacheChannel.set(region, key, value);
    }

    @Override
    public void invalidate(String region, String... key) {
        cacheChannel.evict(region, key);
    }

    @Override
    public void invalidateRegion(String region) {
        cacheChannel.clear(region);
    }

    @Override
    public void invalidateAll() {
        cacheChannel.regions().stream().map(CacheChannel.Region::getName).forEach(this::invalidateRegion);
    }

    @Override
    public boolean exists(String region, String key) {
        return cacheChannel.exists(region, key);
    }
}
