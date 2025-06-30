package com.dstz.component.pubsub.redis;

import com.dstz.component.pubsub.AbMessagePublisher;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis message publishing implementation
 *
 */
public class RedisMessagePublishProvider implements AbMessagePublisher {

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisMessagePublishProvider(RedisConnectionFactory redisConnectionFactory) {
		redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	}

	@Override
	public void publish(String channel, Object body) {
		redisTemplate.convertAndSend(channel, body);
	}
}
