package com.dstz.component.mq.msg.engine.api.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.constats.AbCacheRegionConstant;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.msg.engine.constants.MsgCackeKeyConstant;
import com.dstz.component.mq.msg.engine.core.entity.MessageTemplate;
import com.dstz.component.mq.msg.engine.core.entity.model.TemplateParamData;
import com.dstz.component.mq.msg.engine.core.manager.MessageTemplateManager;
import com.dstz.component.msg.api.MessageTemplateApi;
import com.dstz.component.msg.api.vo.CardTemplateData;
import com.dstz.component.msg.api.vo.MessageTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

import static com.dstz.component.mq.msg.engine.constants.MsgEngineStatusCode.GET_DATA_BY_CODE_IS_NULL;
import static com.dstz.component.mq.msg.engine.constants.MsgEngineStatusCode.PARAM_TEMPLATE_CODE_MISS;

/**
 * Business message implementation
 *
 */
@Service
public class MessageTemplateApiImpl implements MessageTemplateApi {

    @Autowired
    MessageTemplateManager messageTemplateManager;

    /**
     * Get template information from cache according to code
     *
     * @param code
     * @return
     */
    @Cacheable(cacheNames = AbCacheRegionConstant.MSG_REGION, key = MsgCackeKeyConstant.ET_MESSAGE_TEMPLATE_LIST_RECEIVE_EL)
    public MessageTemplateVO getTemplateByCodeInCache(String code) {
        Assert.notBlank(code, () -> new BusinessMessage(PARAM_TEMPLATE_CODE_MISS));
        MessageTemplate messageTemplate = messageTemplateManager.selectOne(Wrappers.lambdaQuery(MessageTemplate.class).eq(MessageTemplate::getCode, code));
        Assert.notNull(messageTemplate, () -> new BusinessMessage(GET_DATA_BY_CODE_IS_NULL.formatDefaultMessage(code)));
        MessageTemplateVO templateVO = new MessageTemplateVO();
        BeanCopierUtils.copyProperties(messageTemplate, templateVO);
        if (StrUtil.isNotBlank(messageTemplate.getTemplateParam())) {
            Map<String, String> paramMap = JsonUtils.parseArray(messageTemplate.getTemplateParam(), TemplateParamData.class).stream().collect(Collectors.toMap(TemplateParamData::getKey, TemplateParamData::getValue));
            templateVO.setTemplateParamJson(JsonUtils.toJSONString(paramMap));
        }
        return templateVO;
    }

    @Override
    public MessageTemplateVO getTemplateByCode(String code) {
        return ObjectUtil.clone(this.getTemplateByCodeInCache(code));
    }
}
