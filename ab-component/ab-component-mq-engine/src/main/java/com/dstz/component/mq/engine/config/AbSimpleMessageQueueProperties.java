package com.dstz.component.mq.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Message Queue Configuration
 *
 */
@ConfigurationProperties(prefix = "ab.simple-mq")
public class AbSimpleMessageQueueProperties {

    /**
     * Message Queue Mode
     */
    private AbMessageQueueType messageQueueType = AbMessageQueueType.SYNCHRONOUS;

    public AbMessageQueueType getMessageQueueType() {
        return messageQueueType;
    }

    public void setMessageQueueType(AbMessageQueueType messageQueueType) {
        this.messageQueueType = messageQueueType;
    }
}
