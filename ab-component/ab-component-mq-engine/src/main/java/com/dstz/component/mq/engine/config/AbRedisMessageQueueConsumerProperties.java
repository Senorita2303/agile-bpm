package com.dstz.component.mq.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis message queue consumption attribute configuration
 *
 */
@ConfigurationProperties(prefix = "ab.simple-mq.redis-consumer")
public class AbRedisMessageQueueConsumerProperties {

    /**
     * Name in redisTemplate container
     */
    private String redisTemplateBeanName = "redisTemplate";

    public String getRedisTemplateBeanName() {
        return redisTemplateBeanName;
    }

    public void setRedisTemplateBeanName(String redisTemplateBeanName) {
        this.redisTemplateBeanName = redisTemplateBeanName;
    }
}
