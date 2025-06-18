package com.dstz.component.mq.api.producer;

import com.dstz.component.mq.api.model.JmsDTO;

import java.util.List;

/**
 * Message sending provider
 *
 */
public interface JmsProducer {

    /**
     * Send to queue
     *
     * @param message Send Message
     */
    void sendToQueue(JmsDTO message);

    /**
     * Send a list to the queue
     *
     * @param messages Send message set
     */
    void sendToQueue(List<JmsDTO> messages);

}