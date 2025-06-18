package com.dstz.component.msg.api.dto;

import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.identityconvert.SysIdentity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Template message dto
 * </p>
 *
 */
public class MsgDTO implements Serializable {
	// Title
	private String subject;

	// html template
	private String htmlTemplate;

	// Template key
	private String templateCode;

	// Receiver
	private List<SysIdentity> receivers;

	// Business id
	private String businessId;

	// Business Type Reference com.dstz.base.common.constats.InnerMsgEnum
	private String innerMsgType;

	// Send message type, message implementation such as email
	private List<String> msgType;

	// Formatting required system parameters
	private Object object;

	// Extended parameters
	private Map<String, Object> extendVars = new HashMap<String, Object>();

	public MsgDTO() {
	}

	public MsgDTO(String subject, List<String> msgType, String htmlTemplate, List<SysIdentity> receivers,
			String businessId, String innerMsgType, Object object, Map<String, Object> extendVars) {
		this.subject = subject;
		this.msgType = msgType;
		this.htmlTemplate = htmlTemplate;
		this.receivers = receivers;
		this.businessId = businessId;
		this.innerMsgType = innerMsgType;
		this.object = object;
		this.extendVars = extendVars;
	}

	public MsgDTO(String subject, String templateCode, List<SysIdentity> receivers, List<String> msgType,
			Object object) {
		this.subject = subject;
		this.templateCode = templateCode;
		this.receivers = receivers;
		this.msgType = msgType;
		this.object = object;
	}

	public MsgDTO(String subject, String templateCode, List<SysIdentity> receivers, String businessId,
			String innerMsgType, List<String> msgType, Object object) {
		this.subject = subject;
		this.templateCode = templateCode;
		this.receivers = receivers;
		this.businessId = businessId;
		this.innerMsgType = innerMsgType;
		this.msgType = msgType;
		this.object = object;
	}

	public MsgDTO(String subject, String templateCode, List<SysIdentity> receivers, String businessId,
			String innerMsgType, List<String> msgType, Object object, Map<String, Object> extendVars) {
		this.subject = subject;
		this.templateCode = templateCode;
		this.receivers = receivers;
		this.businessId = businessId;
		this.innerMsgType = innerMsgType;
		this.msgType = msgType;
		this.object = object;
		this.extendVars = extendVars;
	}

	public String getHtmlTemplate() {
		return htmlTemplate;
	}

	public void setHtmlTemplate(String htmlTemplate) {
		this.htmlTemplate = htmlTemplate;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public List<SysIdentity> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<SysIdentity> receivers) {
		this.receivers = receivers;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getInnerMsgType() {
		return innerMsgType;
	}

	public void setInnerMsgType(String innerMsgType) {
		this.innerMsgType = innerMsgType;
	}

	public Map<String, Object> getExtendVars() {
		return extendVars;
	}

	public void setExtendVars(Map<String, Object> extendVars) {
		this.extendVars = extendVars;
	}

	public List<String> getMsgType() {
		return msgType;
	}

	public void setMsgType(List<String> msgType) {
		this.msgType = msgType;
	}
}
