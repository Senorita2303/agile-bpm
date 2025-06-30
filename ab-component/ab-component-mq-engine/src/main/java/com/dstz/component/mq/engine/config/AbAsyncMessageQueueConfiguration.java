package com.dstz.component.mq.engine.config;

import com.dstz.component.mq.engine.producer.AsyncJmsProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * async message queue configuration
 *
 */
@Conditional(AbMessageQueueConditional.class)
public class AbAsyncMessageQueueConfiguration {

	@Bean
	public AsyncJmsProducer asyncJmsProducer(){
		return new AsyncJmsProducer();
	}
}
