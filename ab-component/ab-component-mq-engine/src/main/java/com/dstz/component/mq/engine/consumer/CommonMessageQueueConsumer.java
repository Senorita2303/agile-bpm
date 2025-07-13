package com.dstz.component.mq.engine.consumer;

import com.dstz.component.mq.api.JmsHandler;
import com.dstz.component.mq.api.model.JmsDTO;
import org.springframework.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;

/**
 * General message queue consumption
 *
 */
public class CommonMessageQueueConsumer extends AbstractMessageQueue {


    private static final Logger LOGGER = LoggerFactory.getLogger(CommonMessageQueueConsumer.class);

    /**
     * Loading Complete
     */
    private volatile boolean loadComplete;

    private final byte[] monitor = new byte[0];

    @Override
    protected void containerInitialCompleteAfter() {
        this.loadComplete = true;
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    /**
     * Processing Messages
     *
     * @param jmsDTO jmsDTO
     */
    public void handleMessage(JmsDTO<Serializable> jmsDTO) {
        // Prevent the message from being called before loading is successful
        while (!this.loadComplete) {
            synchronized (monitor) {
                try {
                    monitor.wait(500L);
                } catch (InterruptedException e) {
                    LOGGER.error("Interrupted!", e);
                    Thread.currentThread().interrupt();
                    ReflectionUtils.rethrowRuntimeException(e);
                }
            }
        }
        JmsHandler<Serializable> jmsHandler = getJmsHandler(jmsDTO.getType());
        if (jmsHandler == null) {
            LOGGER.warn("{} no handler", jmsDTO.getType());
            return;
        }
        jmsHandler.handlerMessage(jmsDTO);
    }
}
