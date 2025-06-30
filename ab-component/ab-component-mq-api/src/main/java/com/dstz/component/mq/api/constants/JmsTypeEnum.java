package com.dstz.component.mq.api.constants;

/**
 * Send message type enumeration
 */
public enum JmsTypeEnum {
	INNER("inner"), EMAIL("email"), DING_DING("dingding"), SMS("sms"), WEI_XIN("weixin"), WEI_XIN_QY("weixinQy");

	private final String type;

	JmsTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
