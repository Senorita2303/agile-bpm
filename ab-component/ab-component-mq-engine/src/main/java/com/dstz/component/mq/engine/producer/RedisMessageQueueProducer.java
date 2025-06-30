package com.dstz.component.mq.engine.producer;

import cn.hutool.core.collection.CollectionUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.api.constants.JmsDestinationConstant;
import com.dstz.component.mq.api.model.JmsDTO;
import com.dstz.component.mq.api.producer.JmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * Redis message queue provides implementation
 *
 */
public class RedisMessageQueueProducer implements JmsProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageQueueProducer.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public void sendToQueue(JmsDTO message) {
        if (message == null) {
            LOGGER.info("The input parameter is empty, skip execution");
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JsonUtils.toJSONString(message));
        }
        redisTemplate.boundListOps(JmsDestinationConstant.DEFAULT_NAME).rightPush(message);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sendToQueue(List<JmsDTO> messages) {
        if (CollectionUtil.isEmpty(messages)) {
            LOGGER.info("The input parameter is empty, skip execution");
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(JsonUtils.toJSONString(messages));
        }
        redisTemplate.boundListOps(JmsDestinationConstant.DEFAULT_NAME).rightPushAll(messages.toArray());
    }
}
