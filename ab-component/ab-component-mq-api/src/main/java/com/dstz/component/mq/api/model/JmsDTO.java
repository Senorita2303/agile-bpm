package com.dstz.component.mq.api.model;

import java.io.Serializable;

/**
 * Message transfer object
 *
 * @param <T>
 */
public interface JmsDTO<T extends Serializable> extends Serializable {

    /**
     * Identification of specific consumers
     *
     * @return Consumer Logo
     */
    String getType();

    /**
     * Consumer's data object
     *
     * @return Consumption data
     */
    T getData();
}
