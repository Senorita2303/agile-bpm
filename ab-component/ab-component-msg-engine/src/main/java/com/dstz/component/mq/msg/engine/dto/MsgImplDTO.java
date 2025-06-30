package com.dstz.component.mq.msg.engine.dto;

import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.identityconvert.SysIdentity;
import com.dstz.component.msg.api.vo.CardTemplateData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Template message business implementation dto
 * </p>
 *
 */
public class MsgImplDTO implements Serializable {
	// title
	private String subject;

	// Template parameters
	private Map<String, String> templateParam;

	// HTML template
	private String htmlTemplate;

	// Card Templates
	private CardTemplateData cardTemplateData;

	// Receiver
	private List<SysIdentity> receivers;

	// Business ID
	private String businessId;

	// Business Type Reference com.dstz.base.common.constats.InnerMsgEnum
	private String innerMsgType;

	// Extended Parameters
	private Map<String, Object> extendVars = new HashMap<String, Object>();

	public MsgImplDTO() {
	}

	public MsgImplDTO(String subject, String htmlTemplate, List<SysIdentity> receivers) {
		this.subject = subject;
		this.htmlTemplate = htmlTemplate;
		this.receivers = receivers;
	}

	public MsgImplDTO(String subject, String htmlTemplate, Map<String, Object> extendVars) {
		this.subject = subject;
		this.htmlTemplate = htmlTemplate;
		this.extendVars = extendVars;
	}

	public MsgImplDTO(String subject, String htmlTemplate, CardTemplateData cardTemplateData,
			List<SysIdentity> receivers, String businessId, String innerMsgType, Map<String, Object> extendVars) {
		this.subject = subject;
		this.htmlTemplate = htmlTemplate;
		this.cardTemplateData = cardTemplateData;
		this.receivers = receivers;
		this.businessId = businessId;
		this.innerMsgType = innerMsgType;
		this.extendVars = extendVars;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, String> getTemplateParam() {
		return templateParam;
	}

	public void setTemplateParam(Map<String, String> templateParam) {
		this.templateParam = templateParam;
	}

	public String getHtmlTemplate() {
		return htmlTemplate;
	}

	public void setHtmlTemplate(String htmlTemplate) {
		this.htmlTemplate = htmlTemplate;
	}

	public CardTemplateData getCardTemplateData() {
		return cardTemplateData;
	}

	public void setCardTemplateData(CardTemplateData cardTemplateData) {
		this.cardTemplateData = cardTemplateData;
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
}
