package com.dstz.component.mq.engine.producer;

import cn.hutool.core.collection.CollUtil;
import com.dstz.component.mq.api.model.JmsDTO;
import com.dstz.component.mq.api.producer.JmsProducer;
import com.dstz.component.mq.engine.consumer.AbstractMessageQueue;

import java.util.List;

/**
 * Synchronous queue sending, internal application
 *
 */
public class SynchronousQueueProducer extends AbstractMessageQueue implements JmsProducer {

    @SuppressWarnings("unchecked")
    @Override
    public void sendToQueue(JmsDTO message) {
        invokeJmsHandler(message);
    }

    @Override
    public void sendToQueue(List<JmsDTO> messages) {
        if (CollUtil.isNotEmpty(messages)) {
            messages.forEach(this::invokeJmsHandler);
        }
    }
}
