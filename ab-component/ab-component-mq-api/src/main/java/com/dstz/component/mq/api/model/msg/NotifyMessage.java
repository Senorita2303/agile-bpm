package com.dstz.component.mq.api.model.msg;

import com.dstz.base.common.constats.InnerMsgEnum;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.org.api.model.IUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notification message DTO
 *
 */
public class NotifyMessage implements Serializable {
    private String subject;
    private String htmlContent;
    private String textContent;
    private IUser sender;
    private List<SysIdentity> receivers;
    private String businessId;
    private InnerMsgEnum innerMsgEnum;
    private Map<String, Object> extendVars = new HashMap<String, Object>();


    public NotifyMessage() {
    }

    public NotifyMessage(String subject, String htmlContent, IUser sender, List<SysIdentity> receivers) {
        this.subject = subject;
        this.sender = sender;
        this.receivers = receivers;
        this.htmlContent = htmlContent;
    }

    public NotifyMessage(String subject, String htmlContent, IUser sender, List<SysIdentity> receivers, String businessId) {
        this.subject = subject;
        this.sender = sender;
        this.receivers = receivers;
        this.htmlContent = htmlContent;
        this.businessId = businessId;
    }


    public NotifyMessage(String subject, String htmlContent, IUser sender, List<SysIdentity> receivers, String businessId,InnerMsgEnum innerMsgEnum) {
        this.subject = subject;
        this.sender = sender;
        this.receivers = receivers;
        this.htmlContent = htmlContent;
        this.businessId = businessId;
        this.innerMsgEnum = innerMsgEnum;
    }

    public NotifyMessage(String subject, String htmlContent, String textContent, IUser sender, List<SysIdentity> receivers, String businessId,InnerMsgEnum innerMsgEnum) {
        this.subject = subject;
        this.sender = sender;
        this.receivers = receivers;
        this.htmlContent = htmlContent;
        this.businessId = businessId;
        this.textContent = textContent;
        this.innerMsgEnum = innerMsgEnum;
    }



    public InnerMsgEnum getInnerMsgEnum() {
        return innerMsgEnum;
    }

    public void setInnerMsgEnum(InnerMsgEnum innerMsgEnum) {
        this.innerMsgEnum = innerMsgEnum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public IUser getSender() {
        return sender;
    }

    public void setSender(IUser sender) {
        this.sender = sender;
    }

    public List<SysIdentity> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<SysIdentity> receivers) {
        this.receivers = receivers;
    }

    public Map<String, Object> getExtendVars() {
        return extendVars;
    }

    public void setExtendVars(Map<String, Object> extendVars) {
        this.extendVars = extendVars;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
