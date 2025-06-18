package com.dstz.sys.rest.model.vo;

/**
 * Automatic translation VO
 *
 */
public class AutoTranslateVO implements java.io.Serializable {

	private static final long serialVersionUID = -1414897543240472067L;

	/**
	 * Failure text
	 */
	private String errorMsg;

	/**
	 * Language
	 */
	private String language;

	/**
	 * Translated text
	 */
	private String dstText;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDstText() {
		return dstText;
	}

	public void setDstText(String dstText) {
		this.dstText = dstText;
	}
}
