package com.dstz.component.redis.cache;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.cache.CacheRegion;
import com.dstz.base.common.cache.ICache;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.utils.CastUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Redis cache implementation
 *
 */
public class AbRedisCache implements ICache {

    private final Map<String, Duration> regionExpire;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ConcurrentHashMap<String, Object> keyLocks = new ConcurrentHashMap<>();

    public AbRedisCache(List<CacheRegion> cacheRegionList, RedisConnectionFactory redisConnectionFactory) {
        this.regionExpire = cacheRegionList.stream().collect(Collectors.toMap(CacheRegion::getRegion, CacheRegion::getExpiration));
        this.redisTemplate = new RedisTemplate<>();
        redisTemplate.setEnableDefaultSerializer(false);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(RedisSerializer.string());
        redisTemplate.afterPropertiesSet();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    }

    private Duration getCacheExpire(String region) {
        Duration duration = regionExpire.get(region);
        Assert.notNull(duration, () -> new IllegalArgumentException(String.format("Cache Region %s is not configured", region)));
        return duration;
    }

    private String getCacheKey(String region, String key) {
        return StrUtil.concat(true, region, StrPool.COLON, key);
    }

    @Override
    public <V> V getIfPresent(String region, String key) {
        String cacheKey = getCacheKey(region, key);
        return CastUtils.cast(redisTemplate.opsForValue().get(cacheKey));
    }

    @Override
    public <V> V get(String region, String key, Callable<V> loader) {
        final String cacheKey = getCacheKey(region, key);
        V cacheValue = getIfPresent(region, key);
        if (cacheValue == null) {
            synchronized (keyLocks.computeIfAbsent(cacheKey, k -> new Object())) {
                cacheValue = getIfPresent(region, key);
                if (cacheValue == null) {
                    try {
                        cacheValue = loader.call();
                    } catch (Exception e) {
                        ReflectionUtils.rethrowRuntimeException(e);
                    }
                    Boolean putOk = redisTemplate.opsForValue().setIfAbsent(cacheKey, cacheValue, getCacheExpire(region));
                    if (!Boolean.TRUE.equals(putOk)) {
                        cacheValue = getIfPresent(region, key);
                    }
                }
            }
        }
        return cacheValue;
    }

    @Override
    public void put(String region, String key, Object value) {
        String cacheKey = getCacheKey(region, key);
        Duration cacheExpire = getCacheExpire(region);
        redisTemplate.opsForValue().set(cacheKey, value, cacheExpire);
    }

    @Override
    public void invalidate(String region, String... keys) {
        if (keys.length == 1) {
            redisTemplate.delete(getCacheKey(region, keys[0]));
        } else {
            redisTemplate.delete(Arrays.stream(keys).map(key -> getCacheKey(region, key)).collect(Collectors.toSet()));
        }
    }

    @Override
    public void invalidateRegion(String region) {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(getCacheKey(region, "*")).build();
        try (RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            int deleteCount = 1;
            LinkedList<String> keys = new LinkedList<>();
            for (Cursor<byte[]> cursor = connection.scan(scanOptions); cursor.hasNext(); deleteCount++) {
                keys.add((String) redisTemplate.getKeySerializer().deserialize(cursor.next()));
                if (deleteCount % 1000 == 0) {
                    redisTemplate.delete(keys);
                    keys.clear();
                }
            }
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    @Override
    public void invalidateAll() {
        try (RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            redisConnection.flushDb();
        }
    }

    @Override
    public boolean exists(String region, String key) {
        String cacheKey = getCacheKey(region, key);
        Long expire = redisTemplate.getExpire(cacheKey);
        return Objects.nonNull(expire) && expire > 0L;
    }
}
