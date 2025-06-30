package com.dstz.base.common.events;

import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Objects;

/**
 * User update cache event event
 *
 */
public class AbUserEvent extends ApplicationEvent {
    public AbUserEvent(List<String> userAccount, EventType eventType) {
        super(userAccount);
        this.eventType = Objects.requireNonNull(eventType);
    }

    /**
     * Event Type
     */
    public enum EventType {
        /**
         * Update User
         */
        UPDATE_USER,

        /**
         * Delete User
         */
        DELETE_USER,


    }

    private final EventType eventType;

    public List<String> getUserAccountList() {
        return (List<String>) source;
    }

    public EventType getEventType() {
        return eventType;
    }
}
