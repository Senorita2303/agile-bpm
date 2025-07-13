package com.dstz.component.mq.api;

import com.dstz.component.mq.api.model.JmsDTO;

import java.io.Serializable;

/**
 * <pre>
 * All consumers need to implement this interface<br />
 * Get the specific handler by type
 * </pre>
 *
 */
public interface JmsHandler<T extends Serializable> {

    /**
     * Get message type
     *
     * @return Message Type
     */
    String getType();

    /**
     * Processing Messages
     *
     * @param message Message transmission type
     * @return Successfully processed
     */
    boolean handlerMessage(JmsDTO<T> message);
}
