package com.dstz.component.mq.api.model;

import com.dstz.component.mq.api.constants.JmsTypeEnum;

import java.io.Serializable;

/**
 * Default message
 *
 * @param <T>
 */
public class DefaultJmsDTO<T extends Serializable> implements JmsDTO {


    private String type;

    private T data;

    public DefaultJmsDTO() {
    }

    public DefaultJmsDTO(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public DefaultJmsDTO(JmsTypeEnum jmsTypeEnum, T data) {
        this.type = jmsTypeEnum.getType();
        this.data = data;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
