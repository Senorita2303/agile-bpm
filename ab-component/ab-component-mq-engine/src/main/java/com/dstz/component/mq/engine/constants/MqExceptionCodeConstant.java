package com.dstz.component.mq.engine.constants;

import com.dstz.base.common.codes.IBaseCode;

/**
 * Message queue name
 *
 */
public enum MqExceptionCodeConstant implements IBaseCode {

    SEND_ERROR("send_error", "Message sending failed {}"),
    SYNC_SEND_ERROR("sync_send_error", "- Synchronous message sending failed {}"),
    EMAIL_SEND_ERROR("email_send_error", "- Email sending failed {}"),
    ;

    private final String code;

    private final String message;

    MqExceptionCodeConstant(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
