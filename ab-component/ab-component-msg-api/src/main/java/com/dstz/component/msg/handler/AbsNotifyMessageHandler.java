package com.dstz.component.msg.handler;

import com.dstz.base.common.identityconvert.IdentityConvert;
import com.dstz.component.mq.api.JmsHandler;
import com.dstz.component.mq.api.model.JmsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Do the common logic of message types: such as logs, etc
 *
 * @param <T>
 */
public abstract class AbsNotifyMessageHandler<T extends Serializable> implements JmsHandler<T> {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbsNotifyMessageHandler.class);

	public static final String PARAM_USERNAME = "$userName";

	public static final String PC_URL_CHART = "{ctx}";

	@Autowired
	public IdentityConvert identityConvert;

	/**
	 * Message type name
	 *
	 * @return
	 */
	public abstract String getTitle();

	/**
	 * Is it selected by default?
	 *
	 * @return
	 */
	public boolean getIsDefault() {
		return false;
	}

	/**
	 * Whether to support HTML content
	 *
	 * @return
	 */
	public boolean getSupportHtml() {
		return true;
	}

	@Override
	public boolean handlerMessage(JmsDTO<T> message) {
		return sendMessage(message.getData());
	}

	/**
	 * 发送消息处理器具体实现 不同消息的发送
	 *
	 * @param data
	 * @return
	 */
	public abstract boolean sendMessage(T data);

}
