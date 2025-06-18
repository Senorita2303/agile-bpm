package com.dstz.base.event;

import com.dstz.base.model.AbDataSourceModel;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Before switching data source
 *
 */
public class AbDataSourceEvent extends ApplicationEvent {

    private static final long serialVersionUID = 8850465955737343686L;

    /**
     * Event Type
     */
    public enum EventType {
        /**
         * Before switching data source
         */
        SWITCH_BEFORE,

        /**
         * After the data source is removed
         */
        REMOVE_AFTER,

        /**
         * Before getting the transaction
         */
        GET_BEFORE,
    }

    private final EventType eventType;

    public AbDataSourceEvent(AbDataSourceModel abDataSourceModel, EventType eventType) {
        super(abDataSourceModel);
        this.eventType = Objects.requireNonNull(eventType);
    }

    public AbDataSourceModel getAbDataSourceModel(){
        return (AbDataSourceModel) source;
    }

    public EventType getEventType() {
        return eventType;
    }

    /**
     * If the event type is specified, the function is executed
     *
     * @param eventType Event Type
     * @param consumer  function
     */
    public void ifEventType(EventType eventType, Consumer<AbDataSourceModel> consumer) {
        if (this.eventType.equals(eventType)) {
            consumer.accept((AbDataSourceModel) getSource());
        }
    }


}
