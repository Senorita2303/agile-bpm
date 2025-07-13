package com.dstz.component.msg.api.dto;

import com.dstz.base.common.constats.StrPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * External message dto
 * Do not rely on system users to send messages
 * </p>
 *
 */
public class ExternalMsgDTO {
	// Title
	private String subject;

	// email recipient
	List<String> emailReceivers;

	// SMS recipients
	List<String> smsReceivers;

	private String templateCode;
	// html template
	private String htmlTemplate;

	// File id
	private String emailFileIds;

	// Formatting required system parameters
	private Object object;

	// Extended parameters
	private Map<String, Object> extendVars = new HashMap<String, Object>();

	// Verification code type SMS needs to be passed in extenVars extendVars.put("verification",true) Other types of SMS do not need to be passed in
	public static final String SMSTYPE = "verification";

	public ExternalMsgDTO() {
	}

	public ExternalMsgDTO(String subject, List<String> emailReceivers, List<String> smsReceivers, String htmlTemplate,
			Object object) {
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

	public ExternalMsgDTO(String subject, List<String> emailReceivers, List<String> smsReceivers, String htmlTemplate,
			Map<String, Object> extendVars) {
		this.subject = subject;
		this.emailReceivers = emailReceivers;
		this.smsReceivers = smsReceivers;
		this.htmlTemplate = htmlTemplate;
		this.extendVars = extendVars;
	}

	public ExternalMsgDTO(String subject, String templateCode, List<String> emailReceivers, List<String> smsReceivers,
			Object object, String emailFileIds) {
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
