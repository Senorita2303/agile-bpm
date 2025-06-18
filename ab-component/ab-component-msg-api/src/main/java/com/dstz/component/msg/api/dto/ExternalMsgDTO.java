package com.dstz.component.msg.api.dto;

import com.dstz.base.common.constats.StrPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 外部消息dto
 * 不依赖系统用户发送消息
 * </p>
 *
 * @author lightning
 * @since 2023-07-06
 */
public class ExternalMsgDTO {
    //标题
    private String subject;

    //email接收人
    List<String> emailReceivers;

    //短信接收人
    List<String> smsReceivers;

    private String templateCode;
    //html模板
    private String htmlTemplate;

    //文件id
    private String emailFileIds;

    //格式化所需系统参数
    private Object object;



    //扩展参数
    private Map<String, Object> extendVars = new HashMap<String, Object>();

    //验证码类型短信需要在extenVars中传入 extendVars.put("verification",true) 其他类型短信无需传入
    public static final String SMSTYPE =  "verification";

    public ExternalMsgDTO() {
    }


    public ExternalMsgDTO(String subject, List<String> emailReceivers, List<String> smsReceivers, String htmlTemplate, Object object) {
        this.subject = subject;
        this.emailReceivers = emailReceivers;
        this.smsReceivers = smsReceivers;
        this.htmlTemplate = htmlTemplate;
        this.object = object;
    }

    public ExternalMsgDTO(String subject, List<String> emailReceivers, String templateCode, Object object) {
        this.subject = subject;
        this.emailReceivers = emailReceivers;
        this.templateCode = templateCode;
        this.object = object;

    }


    public ExternalMsgDTO(List<String> smsReceivers, String templateCode, Object object) {
        this.smsReceivers = smsReceivers;
        this.templateCode = templateCode;
        this.object = object;
    }

    public ExternalMsgDTO(String subject, List<String> emailReceivers, List<String> smsReceivers, String htmlTemplate,Map<String, Object> extendVars) {
        this.subject = subject;
        this.emailReceivers = emailReceivers;
        this.smsReceivers = smsReceivers;
        this.htmlTemplate = htmlTemplate;
        this.extendVars = extendVars;
    }


    public ExternalMsgDTO(String subject, String templateCode, List<String> emailReceivers, List<String> smsReceivers, Object object,String emailFileIds) {
        this.subject = subject;
        this.emailReceivers = emailReceivers;
        this.smsReceivers = smsReceivers;
        this.templateCode = templateCode;
        this.object = object;
        this.emailFileIds = emailFileIds;
    }



    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getHtmlTemplate() {
        return htmlTemplate;
    }

    public void setHtmlTemplate(String htmlTemplate) {
        this.htmlTemplate = htmlTemplate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getEmailReceivers() {
        return emailReceivers;
    }

    public void setEmailReceivers(List<String> emailReceivers) {
        this.emailReceivers = emailReceivers;
    }

    public List<String> getSmsReceivers() {
        return smsReceivers;
    }

    public void setSmsReceivers(List<String> smsReceivers) {
        this.smsReceivers = smsReceivers;
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Map<String, Object> getExtendVars() {
        return extendVars;
    }

    public void setExtendVars(Map<String, Object> extendVars) {
        this.extendVars = extendVars;
    }

    public String getEmailFileIds() {
        return emailFileIds;
    }

    public void setEmailFileIds(String emailFileIds) {
        this.emailFileIds = emailFileIds;
    }
}
