package com.dstz.component.mq.msg.engine.constants;

/**
 * System cache key definition
 *
 */
public class MsgCackeKeyConstant {

    /**
     * Message template method cache key SpEL expression
     */
    public static final String GET_MESSAGE_TEMPLATE_LIST = "'getMessageTemplateListEl:'";

    /**
     * Dictionary method cache key SpEL expression
     */
    public static final String ET_MESSAGE_TEMPLATE_LIST_RECEIVE_EL = GET_MESSAGE_TEMPLATE_LIST + ".concat(#root.args[0])";

}
