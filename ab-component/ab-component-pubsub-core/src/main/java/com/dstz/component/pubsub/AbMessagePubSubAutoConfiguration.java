package com.dstz.component.pubsub;

import com.dstz.base.common.utils.CastUtils;
import com.dstz.component.pubsub.local.AbLocalMessagePublishProvider;
import com.dstz.component.pubsub.redis.RedisMessagePublishProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.stream.Collectors;

/**
 * Message publishing and subscription automatic assembly
 *
 */
@Configuration(proxyBeanMethods = false)
public class AbMessagePubSubAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AbMessagePublisher localMessagePublishProvider(ObjectProvider<AbMessageSubscriber<?>> subscriberObjectProvider) {
		return new AbLocalMessagePublishProvider(subscriberObjectProvider.stream().collect(Collectors.toList()));
	}


	@ConditionalOnBean(RedisConnectionFactory.class)
	@Configuration
	protected static class AbRedisMessagePubsubConfiguration {

		private void registerMessageListener(RedisMessageListenerContainer redisMessageListenerContainer, ObjectProvider<AbMessageSubscriber<?>> subscriberObjectProvider) {
			GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
			for (AbMessageSubscriber<?> abMessageSubscriber : subscriberObjectProvider) {
				redisMessageListenerContainer.addMessageListener((message, pattern) -> abMessageSubscriber.subscribe(CastUtils.cast(jackson2JsonRedisSerializer.deserialize(message.getBody()))), new ChannelTopic(abMessageSubscriber.getChannel()));
			}
		}

		@Bean
		public RedisMessagePublishProvider redisMessagePublishProvider(RedisConnectionFactory redisConnectionFactory, ObjectProvider<AbMessageSubscriber<?>> subscriberObjectProvider, @Autowired(required = false) RedisMessageListenerContainer redisMessageListenerContainer) {
			if (redisMessageListenerContainer != null) {
				registerMessageListener(redisMessageListenerContainer, subscriberObjectProvider);
			}
			return new RedisMessagePublishProvider(redisConnectionFactory);
		}

		@ConditionalOnMissingBean
		@Bean
		public RedisMessageListenerContainer redisMessageListenerContainer(ObjectProvider<AbMessageSubscriber<?>> subscriberObjectProvider) {
			ThreadPoolTaskExecutor taskExecutor = new TaskExecutorBuilder()
					.corePoolSize(1)
					.keepAlive(Duration.ofSeconds(60))
					.threadNamePrefix("redis-message-task-")
					.maxPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1)
					.queueCapacity(1000)
					.build();
			RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
			redisMessageListenerContainer.setTaskExecutor(taskExecutor);
			registerMessageListener(redisMessageListenerContainer, subscriberObjectProvider);
			return redisMessageListenerContainer;
		}
	}
}
