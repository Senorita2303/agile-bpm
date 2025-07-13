package com.dstz.component.mq.engine.producer;

import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.engine.constants.MqExceptionCodeConstant;
import com.dstz.component.mq.api.model.JmsDTO;
import com.dstz.component.mq.api.producer.JmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Abandoning queue providers
 *
 */
public class DiscardQueueProducer implements JmsProducer {

    private static final Logger logger = LoggerFactory.getLogger(DiscardQueueProducer.class);

    @Override
    public void sendToQueue(JmsDTO message) throws ApiException {
        try {
            if (logger.isInfoEnabled()) {
                sendToQueue(Collections.singletonList(message));
            }
        } catch (Exception e) {
            throw new BusinessException(MqExceptionCodeConstant.SEND_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }

    @Override
    public void sendToQueue(List<JmsDTO> messages) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info("discard message: {}", JsonUtils.toJSONString(messages));
            }
        } catch (Exception e) {
            throw new BusinessException(MqExceptionCodeConstant.SEND_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }
}
