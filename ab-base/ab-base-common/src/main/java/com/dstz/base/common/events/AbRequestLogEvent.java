package com.dstz.base.common.events;

import com.dstz.base.common.requestlog.AbRequestLog;
import com.dstz.base.common.utils.ToStringUtils;
import org.springframework.context.ApplicationEvent;

/**
 * Request log events
 *
 */
public class AbRequestLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2467689461606762863L;

    /**
     * Event Type
     */
    public enum EventType {
        /**
         *
         */
        PRE_PROCESS,

        /**
         * After request processing
         */
        POST_PROCESS
    }

    private final EventType eventType;


    private AbRequestLogEvent(AbRequestLog abRequestLog, EventType eventType) {
        super(abRequestLog);
        this.eventType = eventType;
    }

    /**
     * Create a pre-request processing event
     *
     * @param abRequestLog Request log
     * @return Request log events
     */
    public static AbRequestLogEvent createPreProcess(AbRequestLog abRequestLog) {
        return new AbRequestLogEvent(abRequestLog, EventType.PRE_PROCESS);
    }

    /**
     * Create a request processing post-event
     *
     * @param abRequestLog Request log
     * @return Request log events
     */
    public static AbRequestLogEvent createPostProcess(AbRequestLog abRequestLog) {
        return new AbRequestLogEvent(abRequestLog, EventType.POST_PROCESS);
    }

    /**
     * Get event type
     *
     * @return Event Type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Get request log
     *
     * @return Request log
     */
    public AbRequestLog getRequestLog() {
        return (AbRequestLog) getSource();
    }

    @Override
    public String toString() {
        return ToStringUtils.toString(this);
    }
}
