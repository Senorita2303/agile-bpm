package com.dstz.component.msg.api.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * Message template dto
 * </p>
 *
 */
public class CardTemplateData implements Serializable {
	private String cardTitle;
	private String cardUrl;
	private String cardContent;
	private String pcCardUrl;
	private Map<String, Object> extendParam;

	public CardTemplateData() {
	}

	public CardTemplateData(String cardTitle, String cardUrl, String cardContent) {
		this.cardTitle = cardTitle;
		this.cardUrl = cardUrl;
		this.cardContent = cardContent;
	}

	public CardTemplateData(String cardTitle, String cardUrl, String cardContent, Map<String, Object> extendParam) {
		this.cardTitle = cardTitle;
		this.cardUrl = cardUrl;
		this.cardContent = cardContent;
		this.extendParam = extendParam;
	}

	public CardTemplateData(String cardTitle, String cardUrl, String cardContent, String pcCardUrl,
			Map<String, Object> extendParam) {
		this.cardTitle = cardTitle;
		this.cardUrl = cardUrl;
		this.cardContent = cardContent;
		this.pcCardUrl = pcCardUrl;
		this.extendParam = extendParam;
	}

	public String getPcCardUrl() {
		return pcCardUrl;
	}

	public void setPcCardUrl(String pcCardUrl) {
		this.pcCardUrl = pcCardUrl;
	}

	public String getCardTitle() {
		return cardTitle;
	}

	public void setCardTitle(String cardTitle) {
		this.cardTitle = cardTitle;
	}

	public String getCardUrl() {
		return cardUrl;
	}

	public void setCardUrl(String cardUrl) {
		this.cardUrl = cardUrl;
	}

	public String getCardContent() {
		return cardContent;
	}

	public void setCardContent(String cardContent) {
		this.cardContent = cardContent;
	}

	public Map<String, Object> getExtendParam() {
		return extendParam;
	}

	public void setExtendParam(Map<String, Object> extendParam) {
		this.extendParam = extendParam;
	}
}
