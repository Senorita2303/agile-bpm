package com.dstz.component.mq.engine.producer;

import cn.hutool.core.collection.CollectionUtil;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.engine.constants.MqExceptionCodeConstant;
import com.dstz.component.mq.api.constants.JmsDestinationConstant;
import com.dstz.component.mq.api.model.JmsDTO;
import com.dstz.component.mq.api.producer.JmsProducer;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.util.List;

/**
 * Specific implementation of general message queue message sending
 *
 */
public class CommonMessageQueueProducer implements JmsProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonMessageQueueProducer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void sendToQueue(JmsDTO message) {
        if (message == null) {
            LOGGER.info("The input parameter is empty, skip execution");
            return;
        }
        try {
            MethodUtils.invokeMethod(jmsTemplate, "convertAndSend", JmsDestinationConstant.DEFAULT_NAME, message);
        } catch (Exception e) {
            LOGGER.warn("JMS sending failed, sending parameters: {}", JsonUtils.toJSONString(message));
            throw new BusinessException(MqExceptionCodeConstant.SEND_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }

    @Override
    public void sendToQueue(List<JmsDTO> messages) {
        if (CollectionUtil.isEmpty(messages)) {
            LOGGER.info("传入参数为空, 跳过执行");
            return;
        }
        for (JmsDTO jmsDTO : messages) {
            sendToQueue(jmsDTO);
        }
    }
}
