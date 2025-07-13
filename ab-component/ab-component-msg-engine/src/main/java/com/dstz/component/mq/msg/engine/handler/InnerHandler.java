package com.dstz.component.mq.msg.engine.handler;

import static com.dstz.base.common.enums.ErrorLogLeve.ERROR;
import static com.dstz.base.common.events.AbErrorLogEvent.createErrorLog;
import static com.dstz.component.mq.api.constants.JmsTypeEnum.INNER;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dstz.base.common.constats.InnerMsgEnum;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.enums.IdentityType;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.component.mq.msg.engine.constants.MsgEngineStatusCode;
import com.dstz.component.mq.msg.engine.dto.MsgImplDTO;
import com.dstz.component.msg.handler.AbsNotifyMessageHandler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;

/**
 * Internal message processor
 *
 */
@Component("innerHandler")
public class InnerHandler extends AbsNotifyMessageHandler<MsgImplDTO> {


    private static final Logger LOGGER = LoggerFactory.getLogger(InnerHandler.class);

    @Override
    public String getType() {
        return INNER.getType();
    }

    @Override
    public String getTitle() {
        return "Internal Message";
    }

    @Override
    public boolean getIsDefault() {
        return false;
    }

    @Override
    public boolean getSupportHtml() {
        return true;
    }


    public boolean handlerMessage() {
        return false;
    }

    @Override
    public boolean sendMessage(MsgImplDTO message) {
        Set<String> userIdList = new HashSet<>();
        identityConvert.convert2Users(message.getReceivers()).forEach(s -> userIdList.add(s.getUserId()));
        StringBuilder builder = new StringBuilder();
        for (SysIdentity receiver : message.getReceivers()) {
            String orgName = IdentityType.fromKey(receiver.getType()).getValue();
            if (builder.length() > 0) {
                builder.append("，");
            }
            builder.append(orgName).append("【").append(receiver.getName()).append("】");
        }
        if (CollectionUtil.isEmpty(userIdList)) {
//            SpringUtil.publishEvent(createErrorLog(new BusinessException(MQ_SEND_FAIL
//                    .formatDefaultMessage("Internal message sending failed: missing actual recipient." + builder + "The extracted user is empty!")), ERROR));
            return false;
        }
        // The in-station message type enumeration cannot be empty
        if (StrUtil.isBlank(message.getInnerMsgType())) {
            SpringUtil.publishEvent(createErrorLog(new BusinessException(MsgEngineStatusCode.MQ_SEND_FAIL.formatDefaultMessage("Missing in-site message type")), ERROR));
            return false;
        }
        // Business ID cannot be empty
     /*   if (StrUtil.isEmpty(message.getBusinessId())) {
            SpringUtil.publishEvent(createErrorLog(new BusinessException(MsgEngineStatusCode.MQ_SEND_FAIL.formatDefaultMessage("Missing the ID of the business associated with the in-site message")), ERROR));
            return false;
        }*/

        // Title, content, type, user set to be pushed, business ID
		/*
		 * cmsInnerMsgApi.save( message.getSubject(),
		 * message.getHtmlTemplate().replace(PC_URL_CHART, StrPool.EMPTY),
		 * InnerMsgEnum.getByKey(message.getInnerMsgType()), userIdList,
		 * message.getBusinessId());
		 */
        LOGGER.info("The message in the station was saved successfully");
        return true;
    }
}
