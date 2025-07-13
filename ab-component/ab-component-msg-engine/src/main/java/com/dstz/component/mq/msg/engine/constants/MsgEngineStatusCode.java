package com.dstz.component.mq.msg.engine.constants;

import com.dstz.base.common.codes.IBaseCode;

/**
 * msg implements relevant response codes
 *
 */
public enum MsgEngineStatusCode implements IBaseCode {



    PARAM_TEMPLATE_CODE_MISS("param_template_code_miss","Template code parameter is missing"),
    GET_DATA_BY_CODE_IS_NULL("get_data_by_code_is_null","No message template data found according to code: {}"),
    TEMPLATE_PARAM_FORMAT_ERROR("template_param_format_error","The template parameter format is incorrect. Please pass in the template parameter in the correct format."),
    NOT_FIND_LOG_REALIZE("not_find_log_realize","Unable to find implementation class for uploader of type [{}]"),
    MSG_LOG_ERROR("msg_log_error","Message logging failed{}"),
    UPDATE_MSG_LOG_STATUS_ERR("update_msg_log_status_err","Update message logging failed{}"),
    MQ_SEND_FAIL("mq_send_fail", "Internal message sending failed, missing required parameters: {}"),

    SMS_SEND_FAIL("sms_send_fail", "SMS sending failed: {}"),

    EMAIL_SEND_FAIL("email_send_fail", "Email sending failed: {}"),

    MQ_SEND_ERROR("mq_send_error", "Message sending failed: {}"),

    ;


    private final String code;
    private final String desc;

    MsgEngineStatusCode(String code, String description) {
        this.code = code;
        this.desc = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return desc;
    }
}
