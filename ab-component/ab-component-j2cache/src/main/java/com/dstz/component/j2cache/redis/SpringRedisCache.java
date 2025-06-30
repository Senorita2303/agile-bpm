package com.dstz.component.j2cache.redis;

import cn.hutool.core.util.StrUtil;
import net.oschina.j2cache.Level2Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * spring redis cache
 *
 */
class SpringRedisCache implements Level2Cache {

    private final String namespace;

    private final String region;

    private final Duration timeout;

    private final RedisTemplate<String, Object> redisTemplate;

    public SpringRedisCache(String namespace, String region, Duration timeout, RedisTemplate<String, Object> redisTemplate) {
        this.namespace = namespace;
        this.region = region;
        this.timeout = timeout;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean supportTTL() {
        return true;
    }

    private String getCacheKey(String key) {
        if (StrUtil.isEmpty(namespace)) {
            return StrUtil.concat(true, region, StrUtil.COLON, key);
        } else {
            return StrUtil.concat(true, namespace, StrUtil.COLON, region, StrUtil.COLON, key);
        }
    }

    @Override
    public byte[] getBytes(String key) {
        return (byte[]) redisTemplate.boundValueOps(getCacheKey(key)).get();
    }

    @Override
    public List<byte[]> getBytes(Collection<String> keys) {
        return keys.stream().map(this::getBytes).collect(Collectors.toList());
    }

    @Override
    public void setBytes(String key, byte[] bytes) {
        redisTemplate.boundValueOps(getCacheKey(key)).set(bytes, timeout);
    }

    @Override
    public void setBytes(Map<String, byte[]> cacheMap) {
        cacheMap.forEach((key, bytes) -> setBytes(getCacheKey(key), bytes));
    }

    @Override
    public Collection<String> keys() {
        List<String> keys;
        try (RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(getCacheKey(StrUtil.EMPTY)).build();
            keys = new LinkedList<>();
            for (Cursor<byte[]> cursor = redisConnection.scan(scanOptions); cursor.hasNext(); ) {
                keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
        }
        return keys;
    }

    @Override
    public void evict(String... keys) {
        List<String> keyList = Arrays.stream(keys).map(this::getCacheKey).collect(Collectors.toList());
        redisTemplate.delete(keyList);
    }

    @Override
    public void clear() {
        try (RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()) {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(getCacheKey(StrUtil.EMPTY)).build();
            int deleteKeyCount = 0;
            LinkedList<String> keys = new LinkedList<>();
            for (Cursor<byte[]> cursor = redisConnection.scan(scanOptions); cursor.hasNext(); deleteKeyCount++) {
                keys.add((String) redisTemplate.getKeySerializer().deserialize(cursor.next()));
                if (deleteKeyCount % 1000 == 0) {
                    redisTemplate.delete(keys);
                    keys.clear();
                }
            }
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    public String getRegion() {
        return region;
    }

    public Duration getTimeout() {
        return timeout;
    }
}
