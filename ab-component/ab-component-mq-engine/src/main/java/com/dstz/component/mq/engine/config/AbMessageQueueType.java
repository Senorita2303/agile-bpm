package com.dstz.component.mq.engine.config;

import com.dstz.component.mq.engine.producer.AsyncJmsProducer;

/**
 * Message queue type
 *
 */
public enum AbMessageQueueType {

    /**
     * redis
     */
    REDIS(AbRedisMessageQueueConfiguration.class),

    /**
     * Java message queue
     */
    JMS(AbMessageQueueConfiguration.class),

    /**
     * Synchronous mode
     */
    SYNCHRONOUS(AbSynchronousMessageQueueConfiguration.class),


    /**
     * Discarding messages
     */
    DISCARD(DiscardMessageQueueConfiguration.class),

    /**
     * Asynchronous Processing
     */
    ASYNC(AsyncJmsProducer.class);

    /**
     * Configuration Class
     */
    private Class<?> configurationClass;

    AbMessageQueueType(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
    }

    public Class<?> getConfigurationClass() {
        return configurationClass;
    }
}
