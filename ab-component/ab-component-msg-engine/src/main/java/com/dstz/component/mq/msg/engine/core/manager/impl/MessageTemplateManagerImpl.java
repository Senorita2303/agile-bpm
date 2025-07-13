package com.dstz.component.mq.msg.engine.core.manager.impl;

import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.component.mq.msg.engine.core.entity.MessageTemplate;
import com.dstz.component.mq.msg.engine.core.manager.MessageTemplateManager;
import org.springframework.stereotype.Service;

/**
 * Generic service implementation class
 *
 */
@Service("messageTemplateManager")
public class MessageTemplateManagerImpl extends AbBaseManagerImpl<MessageTemplate> implements MessageTemplateManager {

}
