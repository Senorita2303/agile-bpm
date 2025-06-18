package com.dstz.component.msg.api.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Message template dto
 * </p>
 *
 */
public class MessageTemplateVO implements Serializable {

	/**
	 * Template code
	 */
	private String code;

	/**
	 * Template name
	 */
	private String name;

	/**
	 * Template description
	 */
	private String desc;

	/**
	 * html template configuration
	 */
	private String htmlTemplate;

	/**
	 * Application template configuration
	 */
	private String appTemplate;

	/**
	 * Card template configuration
	 */
	private String cardTemplate;

	/**
	 * Template parameters
	 */
	private String templateParamJson;

	/**
	 * SMS provider template code
	 */
	private String smsTemplateCode;

	public String getSmsTemplateCode() {
		return smsTemplateCode;
	}

	public void setSmsTemplateCode(String smsTemplateCode) {
		this.smsTemplateCode = smsTemplateCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getHtmlTemplate() {
		return htmlTemplate;
	}

	public void setHtmlTemplate(String htmlTemplate) {
		this.htmlTemplate = htmlTemplate;
	}

	public String getAppTemplate() {
		return appTemplate;
	}

	public void setAppTemplate(String appTemplate) {
		this.appTemplate = appTemplate;
	}

	public String getTemplateParamJson() {
		return templateParamJson;
	}

	public void setTemplateParamJson(String templateParamJson) {
		this.templateParamJson = templateParamJson;
	}

	public String getCardTemplate() {
		return cardTemplate;
	}

	public void setCardTemplate(String cardTemplate) {
		this.cardTemplate = cardTemplate;
	}
}
