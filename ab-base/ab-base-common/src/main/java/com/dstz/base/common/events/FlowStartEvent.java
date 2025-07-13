package com.dstz.base.common.events;

import com.dstz.base.common.utils.MapUtil;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class FlowStartEvent extends ApplicationEvent {
    private static final long serialVersionUID = 7879588828046225244L;

    private final String defKey;
    private final EventType eventType;
    /**
     * Event Type
     */
    public enum EventType {
        /**
         * Start the process by business ID
         */
        BIZ_ID_START,

        /**
         * Initiate processes with business data
         */
        BIZ_DATA_START
    }

    private FlowStartEvent(String defKey, Object source, EventType eventType) {
        super(source);
        this.defKey = defKey;
        this.eventType = eventType;
    }

    public static FlowStartEvent createBizIdStartFlowEvent(String defKey, String bizId) {
        return new FlowStartEvent(defKey,bizId,EventType.BIZ_ID_START);
    }

    public static FlowStartEvent createBizDataStartFlowEvent(String defKey, String boCode, Map<String,Object> data) {
        Map<String,Map<String,Object>> boData = MapUtil.newHashMap(1);
        boData.put(boCode,data);
        return new FlowStartEvent(defKey,boData,EventType.BIZ_DATA_START);
    }

    public String getDefKey() {
        return defKey;
    }

    public EventType getEventType() {
        return eventType;
    }
}
