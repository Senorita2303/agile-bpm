package com.dstz.component.msg.handler;

import com.dstz.base.common.identityconvert.IdentityConvert;
import com.dstz.component.mq.api.JmsHandler;
import com.dstz.component.mq.api.model.JmsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 做消息类型的公共逻辑 ：如日志等
 *
 * @param <T>
 * @author lightning
 */
public abstract class AbsNotifyMessageHandler<T extends Serializable> implements JmsHandler<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbsNotifyMessageHandler.class);
    
    public static final String PARAM_USERNAME = "$userName";

    public static final String PC_URL_CHART = "{ctx}";

    @Autowired
    public IdentityConvert identityConvert;

    /**
     * 消息类型名
     *
     * @return
     */
    public abstract String getTitle();

    /**
     * 是否默认选中
     *
     * @return
     */
    public boolean getIsDefault() {
        return false;
    }

    /**
     * 是否支持 HTML 内容
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
