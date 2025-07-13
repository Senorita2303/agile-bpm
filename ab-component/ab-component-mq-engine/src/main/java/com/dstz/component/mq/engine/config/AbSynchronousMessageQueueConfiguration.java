package com.dstz.component.mq.engine.config;

import com.dstz.component.mq.api.producer.JmsProducer;
import com.dstz.component.mq.engine.producer.SynchronousQueueProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * Synchronous message queue configuration
 *
 */
@Conditional(AbMessageQueueConditional.class)
public class AbSynchronousMessageQueueConfiguration {

    /**
     * Default messaging provider
     *
     * @return Message sending provider
     */
    @Bean
    public JmsProducer jmsProducer() {
        return new SynchronousQueueProducer();
    }

}
