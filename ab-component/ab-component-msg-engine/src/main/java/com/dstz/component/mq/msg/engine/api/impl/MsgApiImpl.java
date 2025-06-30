package com.dstz.component.mq.msg.engine.api.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.ApiException;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.freemark.impl.FreemarkerEngine;
import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.api.JmsHandler;
import com.dstz.component.mq.api.constants.JmsTypeEnum;
import com.dstz.component.mq.api.model.DefaultJmsDTO;
import com.dstz.component.mq.api.model.JmsDTO;
import com.dstz.component.mq.api.producer.JmsProducer;
import com.dstz.component.mq.engine.constants.MqExceptionCodeConstant;
import com.dstz.component.mq.engine.producer.SynchronousQueueProducer;
import com.dstz.component.mq.msg.engine.IMsgCallBack;
import com.dstz.component.mq.msg.engine.core.entity.AbMessageLog;
import com.dstz.component.mq.msg.engine.core.manager.AbMessageLogManager;
import com.dstz.component.mq.msg.engine.dto.MsgImplDTO;
import com.dstz.component.msg.api.MessageTemplateApi;
import com.dstz.component.msg.api.MsgApi;
import com.dstz.component.msg.api.dto.ExternalMsgDTO;
import com.dstz.component.msg.api.dto.MsgDTO;
import com.dstz.component.msg.api.vo.CardTemplateData;
import com.dstz.component.msg.api.vo.MessageTemplateVO;
import com.dstz.component.msg.handler.AbsNotifyMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

import static com.dstz.base.common.enums.ErrorLogLeve.ERROR;
import static com.dstz.base.common.events.AbErrorLogEvent.createErrorLog;
import static com.dstz.component.mq.api.constants.JmsTypeEnum.DING_DING;
import static com.dstz.component.mq.api.constants.JmsTypeEnum.SMS;
import static com.dstz.component.mq.msg.engine.constants.MsgEngineStatusCode.*;
import static com.dstz.component.mq.msg.engine.constants.MsgEnginesConstant.*;

/**
 * Business message implementation
 *
 */
@Service
public class MsgApiImpl implements MsgApi {

    @Autowired
    JmsProducer jmsProducer;

    @Autowired
    MessageTemplateApi messageTemplateApi;

    @Autowired
    FreemarkerEngine freemarkerEngine;

    @Autowired
    AbMessageLogManager messageLogManager;



    private Pattern TMP_PARAM_REGEX = Pattern.compile("(?s).*\\$\\{[^}]+\\}.*");

    /**
     * Single send
     *
     * @param msgDTO
     */
    @Override
    public void sendMsg(MsgDTO msgDTO) {
        if (CollUtil.isNotEmpty(msgDTO.getReceivers())) {
            jmsProducer.sendToQueue(dealMsgDTO(msgDTO));
        }
    }

    /**
     * Batch Send
     *
     * @param msgDTOList
     */
    @Override
    public void sendMsg(List<MsgDTO> msgDTOList) {
        try {
            List<JmsDTO> jmsDTOList = CollUtil.newArrayList();
            msgDTOList.forEach(e -> {
                if (CollUtil.isNotEmpty(e.getReceivers())) {
                    jmsDTOList.addAll(dealMsgDTO(e));
                }
            });
            jmsProducer.sendToQueue(jmsDTOList);
        } catch (Exception e) {
            throw new BusinessException(MqExceptionCodeConstant.SEND_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }


    public String convertTemplateParam(String templateParam, Object object) {
        if (StrUtil.isNotBlank(templateParam) && ReUtil.isMatch(TMP_PARAM_REGEX, templateParam)) {
            return freemarkerEngine.parseByString(templateParam, object);
        }
        return templateParam;
    }

    public String convertTemplateStr(String templateStr, String paramStr) {
        if (StrUtil.isNotBlank(templateStr) && ReUtil.isMatch(TMP_PARAM_REGEX, templateStr)) {
            Map<String, String> paramMap = JsonUtils.parseObject(paramStr, Map.class);
            return freemarkerEngine.parseByString(templateStr, paramMap);
        }
        return templateStr;
    }

    @Override
    public String convertTemplateStr(String templateStr, String templateCode, Object obj) {
        MessageTemplateVO messageTemplateVO = messageTemplateApi.getTemplateByCode(templateCode);
        if (StrUtil.isNotBlank(messageTemplateVO.getTemplateParamJson()) && ReUtil.isMatch(TMP_PARAM_REGEX, messageTemplateVO.getTemplateParamJson())) {
            return convertTemplateStr(templateStr, convertTemplateParam(messageTemplateVO.getTemplateParamJson(), obj));
        }
        return templateStr;
    }

    @Override
    public void sendExtMsg(ExternalMsgDTO extMsgDTO) {
        if (CollUtil.isNotEmpty(extMsgDTO.getSmsReceivers()) || CollUtil.isNotEmpty(extMsgDTO.getEmailReceivers())) {
            jmsProducer.sendToQueue(dealMsgDTO(extMsgDTO));
        }
    }



    @Override
    public void sendExtMsg(List<ExternalMsgDTO> extMsgDTOList) {
        List<JmsDTO> jmsDTOList = CollUtil.newArrayList();
        extMsgDTOList.forEach(e -> {
            if (CollUtil.isNotEmpty(e.getSmsReceivers()) || CollUtil.isNotEmpty(e.getEmailReceivers())) {
                jmsDTOList.addAll(dealMsgDTO(e));
            }
        });
        jmsProducer.sendToQueue(jmsDTOList);
    }
    public JmsHandler getSendType(String type){
        Map<String, JmsHandler> map = SpringUtil.getBeansOfType(JmsHandler.class);
        for (Map.Entry<String, JmsHandler> entry : map.entrySet()) {
            if (entry.getValue().getType().equals(type)) {
                return entry.getValue();
            }
        }
        throw new BusinessException(GlobalApiCodes.INTERNAL_ERROR.formatMessage("找不到类型[{}]的消息实现类", type));
    }
    public void sendMsgByDTO(List<JmsDTO> jmsDTOList){
        jmsDTOList.forEach(e -> {
            getSendType(e.getType()).handlerMessage(e);
        });
    }

    @Override
    public void syncSendMsg(MsgDTO msgDTO) {
        if (CollUtil.isNotEmpty(msgDTO.getReceivers())) {
            sendMsgByDTO(dealMsgDTO(msgDTO));
        }

    }

    @Override
    public void syncSendMsg(List<MsgDTO> msgDTOList) {
        try {
            List<JmsDTO> jmsDTOList = CollUtil.newArrayList();
            msgDTOList.forEach(e -> {
                if (CollUtil.isNotEmpty(e.getReceivers())) {
                    jmsDTOList.addAll(dealMsgDTO(e));
                }
            });
            sendMsgByDTO(jmsDTOList);
        } catch (Exception e) {
            throw new BusinessException(MqExceptionCodeConstant.SEND_ERROR.formatDefaultMessage(e.getMessage()), e);
        }
    }

    @Override
    public void syncSendExtMsg(ExternalMsgDTO extMsgDTO) {
        if (CollUtil.isNotEmpty(extMsgDTO.getSmsReceivers()) || CollUtil.isNotEmpty(extMsgDTO.getEmailReceivers())) {
            sendMsgByDTO(dealMsgDTO(extMsgDTO));
        }
    }

    @Override
    public void syncSendExtMsg(List<ExternalMsgDTO> extMsgDTOList) {
        List<JmsDTO> jmsDTOList = CollUtil.newArrayList();
        extMsgDTOList.forEach(e -> {
            if (CollUtil.isNotEmpty(e.getSmsReceivers()) || CollUtil.isNotEmpty(e.getEmailReceivers())) {
                jmsDTOList.addAll(dealMsgDTO(e));
            }
        });
        sendMsgByDTO(jmsDTOList);
    }

    @Override
    public void updateMsgLogStatusByBusinessId(String businessId, String businessType) {
        try {
            List<AbMessageLog> logs = messageLogManager.getByBusinessId(businessId);
            if (CollUtil.isNotEmpty(logs)) {
                logs.forEach(e -> {
                    // Calling the DingTalk API
                    IMsgCallBack msgCallBack = getMsgCallBack(e.getMsgType());
                    msgCallBack.msgCallBack(e.getOutTrackId(), StrUtil.isEmpty(businessType) ? e.getBusinessType() : businessType);
                    // Modify log status
                    messageLogManager.updateMsgLogStatusByBusinessId(businessId, e.getMsgType());
                });
            }
        } catch (Exception e) {
            SpringUtil.publishEvent(createErrorLog(new BusinessException(UPDATE_MSG_LOG_STATUS_ERR.formatDefaultMessage(e.getMessage())), ERROR));
        }
    }

    public static IMsgCallBack getMsgCallBack(String type) {
        Map<String, IMsgCallBack> map = SpringUtil.getBeansOfType(IMsgCallBack.class);
        for (Map.Entry<String, IMsgCallBack> entry : map.entrySet()) {
            if (entry.getValue().type().equals(type)) {
                return entry.getValue();
            }
        }
        throw new BusinessException(NOT_FIND_LOG_REALIZE.formatDefaultMessage(type));
    }


    private List<JmsDTO> dealMsgDTO(ExternalMsgDTO extMsgDTO) {
        String htmlTemplate = extMsgDTO.getHtmlTemplate();
        String subject = extMsgDTO.getSubject();
        Map<String, String> paramMap = MapUtil.newHashMap();
        if(extMsgDTO.getExtendVars()==null){
            extMsgDTO.setExtendVars(MapUtil.newHashMap());
        }
        if(StrUtil.isNotEmpty(extMsgDTO.getTemplateCode())){
            MessageTemplateVO messageTemplateVO = messageTemplateApi.getTemplateByCode(extMsgDTO.getTemplateCode());
            if(StrUtil.isBlank(extMsgDTO.getHtmlTemplate())){
                extMsgDTO.setHtmlTemplate(messageTemplateVO.getHtmlTemplate());
                // Formatting user-defined parameters
                String paramJson = this.convertTemplateParam(messageTemplateVO.getTemplateParamJson(), extMsgDTO.getObject());
                if (StrUtil.isNotBlank(paramJson)) {
                    paramMap = JsonUtils.parseObject(paramJson, Map.class);
                    extMsgDTO.getExtendVars().putAll(paramMap);
                }
            }
            if(StrUtil.isNotEmpty(messageTemplateVO.getSmsTemplateCode())){
                extMsgDTO.getExtendVars().put("TemplateCode",messageTemplateVO.getSmsTemplateCode());
            }
        }

        if (extMsgDTO.getObject() instanceof Map) {
            ((Map) extMsgDTO.getObject()).putAll(paramMap);
            extMsgDTO.getExtendVars().putAll((Map) extMsgDTO.getObject());
        } else {
            try {
                Map parMap = JsonUtils.toMap(JsonUtils.toJSONString(ObjectUtil.isNotEmpty(extMsgDTO.getObject()) ? extMsgDTO.getObject() : MapUtil.newHashMap()));
                parMap.putAll(paramMap);
                extMsgDTO.setObject(parMap);
                extMsgDTO.getExtendVars().putAll(parMap);
            } catch (Exception e) {
                throw new ApiException(TEMPLATE_PARAM_FORMAT_ERROR);
            }
        }

        // Formatting Template Messages
        if (StrUtil.isNotBlank(extMsgDTO.getHtmlTemplate()) && ReUtil.isMatch(TMP_PARAM_REGEX, extMsgDTO.getHtmlTemplate()) && ObjectUtil.isNotNull(extMsgDTO.getObject())) {
            htmlTemplate = freemarkerEngine.parseByString(extMsgDTO.getHtmlTemplate(), extMsgDTO.getObject());
        }
        // Formatting Titles
        if (StrUtil.isNotBlank(extMsgDTO.getSubject()) && ReUtil.isMatch(TMP_PARAM_REGEX, extMsgDTO.getSubject()) && ObjectUtil.isNotNull(extMsgDTO.getObject())) {
            subject = freemarkerEngine.parseByString(extMsgDTO.getSubject(), extMsgDTO.getObject());
        }
        Map<String, Object> extendVars = MapUtil.newHashMap();
        extendVars.putAll(extMsgDTO.getExtendVars());
        List<String> msgType = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(extMsgDTO.getEmailReceivers())) {
            extendVars.put(EMAIL_RECEIVERS, extMsgDTO.getEmailReceivers());
            msgType.add(JmsTypeEnum.EMAIL.getType());
        }
        if (CollUtil.isNotEmpty(extMsgDTO.getSmsReceivers())) {
            extendVars.put(SMS_RECEIVERS, extMsgDTO.getSmsReceivers());
            msgType.add(SMS.getType());
        }
        if(StrUtil.isNotEmpty(extMsgDTO.getEmailFileIds())){
            extendVars.put(EMAIL_FILE_IDS, extMsgDTO.getEmailFileIds());
        }

        MsgImplDTO msgImplDTO = new MsgImplDTO(subject, htmlTemplate, extendVars);
        List<JmsDTO> msgDTOList = new LinkedList<>();
        msgType.forEach(e -> msgDTOList.add(new DefaultJmsDTO<>(e, msgImplDTO)));
        return msgDTOList;
    }

    private void dealMsgLog(List<JmsDTO> msgDTOList, CardTemplateData cardTemplateData) {
        try {
            msgDTOList.forEach(e -> {
                if (DING_DING.getType().equals(e.getType())) {
                    AbMessageLog messageLog = new AbMessageLog();
                    String bid = cardTemplateData.getExtendParam().get("taskId").toString();
                    String bType = "order";
                    if (cardTemplateData.getExtendParam().containsKey("taskType") && "carbon".equals(cardTemplateData.getExtendParam().get("taskType"))) {
                        bid = cardTemplateData.getExtendParam().get("carbonCopyRecordId").toString();
                        bType = "carbon";
                    } else if (ObjectUtil.isNotEmpty(cardTemplateData.getExtendParam().get("taskType"))) {
                        bType = cardTemplateData.getExtendParam().get("taskType").toString();
                    }
                    messageLog.setBusinessId(bid);
                    messageLog.setOutTrackId(messageLog.getBusinessId());
                    messageLog.setBusinessType(bType);
                    messageLog.setInstId(cardTemplateData.getExtendParam().get("instanceId").toString());
                    messageLog.setCallBackStatus(NumberPool.INTEGER_ZERO);
                    messageLog.setMsgType(DING_DING.getType());
                    messageLogManager.create(messageLog);
                }
            });
        } catch (Exception e) {
            SpringUtil.publishEvent(createErrorLog(new BusinessException(MSG_LOG_ERROR.formatDefaultMessage(e.getMessage())), ERROR));
        }

    }

    // Card custom formatting parameters
    private Map getCustomCardTempParam() {

        String taskId = "${bpmTask.id}";
        String instanceId = "${bpmTask.instId}";
        String taskType = "${taskType}";
        String carbonCopyRecordId = "${carbonCopyRecordId}";
        String nodeKey = "${bpmTask.nodeKey}";
        String taskActionType = "${taskActionType}";
        String reminderType = "${reminderType}";
        String submitOpinion = "${submitOpinion}";
        Map extendParam = MapUtil.newHashMap();
        extendParam.put("carbonCopyRecordId", carbonCopyRecordId);
        extendParam.put("taskId", taskId);
        extendParam.put("instanceId", instanceId);
        extendParam.put("taskType", taskType);
        extendParam.put("bpmTaskType", "${bpmTask.taskType}");
        extendParam.put("nodeKey", nodeKey);
        extendParam.put("taskActionType", taskActionType);
        extendParam.put("reminderType", reminderType);
        extendParam.put("submitOpinion", submitOpinion);
        return extendParam;
    }


    /**
     * Handling request parameters
     *
     * @param msgDTO
     * @return
     */
    private List<JmsDTO> dealMsgDTO(MsgDTO msgDTO) {
        String htmlTemplate = msgDTO.getHtmlTemplate();
        String cardTemplate = JsonUtils.toJSONString(new CardTemplateData(msgDTO.getSubject(), StrPool.EMPTY, msgDTO.getHtmlTemplate(), getCustomCardTempParam()));
        Map<String, String> paramMap = MapUtil.newHashMap();


        if (StrUtil.isNotEmpty(msgDTO.getTemplateCode())) {
            MessageTemplateVO messageTemplateVO = messageTemplateApi.getTemplateByCode(msgDTO.getTemplateCode());
            htmlTemplate = messageTemplateVO.getHtmlTemplate();
            CardTemplateData cardTemplateData = JsonUtils.parseObject(messageTemplateVO.getCardTemplate(), CardTemplateData.class);
            Map map = getCustomCardTempParam();
            map.putAll(JsonUtils.toMap(messageTemplateVO.getTemplateParamJson()));
            cardTemplateData.setExtendParam(map);
            cardTemplate = JsonUtils.toJSONString(cardTemplateData);

            if(StrUtil.isNotEmpty(messageTemplateVO.getSmsTemplateCode())){
                msgDTO.getExtendVars().put("TemplateCode",messageTemplateVO.getSmsTemplateCode());
            }
            // Formatting user-defined parameters 
            String paramJson = this.convertTemplateParam(messageTemplateVO.getTemplateParamJson(), msgDTO.getObject());
            if (StrUtil.isNotBlank(paramJson)) {
                paramMap = JsonUtils.parseObject(paramJson, Map.class);
                msgDTO.getExtendVars().putAll(paramMap);
            }
        }
        // Enter basic parameters
        paramMap.put(PropertyEnum.APP_URL.getKey(), PropertyEnum.APP_URL.getPropertyValue(String.class));
        paramMap.put(PropertyEnum.PC_URL.getKey(), PropertyEnum.PC_URL.getPropertyValue(String.class));

        if (msgDTO.getObject() instanceof Map) {
            ((Map) msgDTO.getObject()).putAll(paramMap);
        } else {
            try {
                Map parMap = JsonUtils.toMap(JsonUtils.toJSONString(ObjectUtil.isNotEmpty(msgDTO.getObject()) ? msgDTO.getObject() : MapUtil.newHashMap()));
                parMap.putAll(paramMap);
                msgDTO.setObject(parMap);
            } catch (Exception e) {
                throw new ApiException(TEMPLATE_PARAM_FORMAT_ERROR);
            }
        }


        if (ObjectUtil.isNotEmpty(msgDTO.getObject())) {
            // Formatting Template Messages
            if (StrUtil.isNotBlank(htmlTemplate) && ReUtil.isMatch(TMP_PARAM_REGEX, htmlTemplate)) {
                htmlTemplate = freemarkerEngine.parseByString(htmlTemplate, msgDTO.getObject());
            }
            if (StrUtil.isNotBlank(cardTemplate) && ReUtil.isMatch(TMP_PARAM_REGEX, cardTemplate)) {
                cardTemplate = freemarkerEngine.parseByString(cardTemplate, msgDTO.getObject());
            }
            // Formatting Titles
            if (StrUtil.isNotBlank(msgDTO.getSubject()) && ReUtil.isMatch(TMP_PARAM_REGEX, msgDTO.getSubject())) {
                msgDTO.setSubject(freemarkerEngine.parseByString(msgDTO.getSubject(), msgDTO.getObject()));
            }
        }


        CardTemplateData cardTemplateData = StrUtil.isNotBlank(cardTemplate) ? JsonUtils.parseObject(cardTemplate, CardTemplateData.class) : new CardTemplateData();
        escapeCardData(msgDTO, cardTemplateData);
        MsgImplDTO msgImplDTO = new MsgImplDTO(msgDTO.getSubject(), htmlTemplate, cardTemplateData, msgDTO.getReceivers(), msgDTO.getBusinessId(), msgDTO.getInnerMsgType(), msgDTO.getExtendVars());
        List<JmsDTO> msgDTOList = new LinkedList<>();
        msgDTO.getMsgType().forEach(e -> msgDTOList.add(new DefaultJmsDTO<>(e, msgImplDTO)));
        dealMsgLog(msgDTOList, cardTemplateData);
        return msgDTOList;
    }

    private void escapeCardData(MsgDTO msgDTO, CardTemplateData data) {
        // Processing json that requires freemarker
        String bpmInstanceSummary = "${bpmInstance.summary}";
        String bpmInstanceSummaryStr = freemarkerEngine.parseByString(bpmInstanceSummary, msgDTO.getObject());
        if (StrUtil.isNotEmpty(bpmInstanceSummaryStr)) {
            data.getExtendParam().put("bpmInstanceSummary", JsonUtils.escapeString(bpmInstanceSummaryStr));
        }
        // Average processing time
        if (ObjectUtil.isNotEmpty(msgDTO.getObject()) && ((HashMap) msgDTO.getObject()).containsKey("def_duration") && data.getExtendParam().containsKey("nodeKey")) {
            List<Map> durationList = JsonUtils.parseArray(JsonUtils.toJSONString(((HashMap) msgDTO.getObject()).get("def_duration")), Map.class);
            String nodeKey = data.getExtendParam().get("nodeKey").toString();
            if (CollUtil.isNotEmpty(durationList)) {
                durationList.forEach(e -> {
                    if (nodeKey.equals(e.get("taskKey"))) {
                        Integer times = Convert.toInt(e.get("duration"));
                        String str =   secondToTime(times)  ; // + ", waiting" +  secondToTime(DateUtil.current()-DateUtil.parse(taskCreateTime,"yyyy-MM-dd HH:mm:ss").getTime());
                        data.getExtendParam().put("defDurationStr", str);
                    }
                });
            }
        }
        // Processing reminder titles and content
        if(data.getExtendParam().containsKey("taskType")){
            // Button Expediting
            if(data.getExtendParam().containsKey("taskActionType") && StrUtil.isNotEmpty(data.getExtendParam().get("taskActionType").toString())){
                // Content: Taken from the notes
                data.getExtendParam().put("bpmInstanceSummary", data.getExtendParam().getOrDefault("submitOpinion","").toString());
            }else{
                // Automatically expediting CC tasks
                if("reminder".equals(data.getExtendParam().get("taskType")) || "carbon".equals(data.getExtendParam().get("taskType"))){
                    data.setCardTitle(msgDTO.getSubject());
                    data.getExtendParam().put("bpmInstanceSummary",data.getCardContent());
                }
            }
        }
        // Process the uuid behind the taskid
        if(data.getExtendParam().containsKey("taskActionType")&&"turn".equals(data.getExtendParam().get("taskActionType"))){
            String taskId = data.getExtendParam().get("taskId").toString();
            if(taskId.contains(StrPool.DASHED)){
                String taskId_new = taskId.split(StrPool.DASHED)[NumberPool.INTEGER_ZERO];
                data.setCardUrl(data.getCardUrl().replace(taskId,taskId_new));
                data.setPcCardUrl(data.getPcCardUrl().replace(taskId,taskId_new));
            }
        }
    }

    public static String secondToTime(long second) {
        long days = second / 86400;            // Conversion Days
        second = second % 86400;            // Remaining seconds
        long hours = second / 3600;            // Convert hours
        second = second % 3600;                // Remaining seconds
        long minutes = second / 60;            // Convert minutes
        second = second % 60;                // Remaining seconds
        if (days > 0) {
            return days + " days " + hours + " hours" ;
        }
        if (hours > 0) {
            return hours + " hours " + minutes + " minute";
        }
        if (minutes > 0) {
            return minutes + " minutes";
        }
        return StrPool.EMPTY;
    }
}
