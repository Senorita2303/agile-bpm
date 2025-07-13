package com.dstz.component.j2cache.redis;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

class RedisTemplateUtils {

    public static RedisTemplate<String, Object> createRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setEnableDefaultSerializer(false);
        redisTemplate.setConnectionFactory(SpringUtil.getBean(RedisConnectionFactory.class));
        redisTemplate.setDefaultSerializer(RedisSerializer.string());
        redisTemplate.afterPropertiesSet();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        return redisTemplate;
    }

}
