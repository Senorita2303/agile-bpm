package com.dstz.component.mq.engine.config;

import com.dstz.component.mq.api.producer.JmsProducer;
import com.dstz.component.mq.engine.producer.DiscardQueueProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * discard message queue configuration
 *
 */
@Conditional(AbMessageQueueConditional.class)
public class DiscardMessageQueueConfiguration {

    @Bean
    public JmsProducer jmsProducer() {
        return new DiscardQueueProducer();
    }
}
