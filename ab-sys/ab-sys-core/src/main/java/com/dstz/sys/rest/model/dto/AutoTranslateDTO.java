package com.dstz.sys.rest.model.dto;


import java.util.List;

import javax.validation.constraints.NotEmpty;

import cn.hutool.core.collection.ListUtil;

/**
 * Automatic translation
 *
 */
public class AutoTranslateDTO implements java.io.Serializable {

	private static final long serialVersionUID = -1866516373715389892L;

	/**
	 * Chinese text
	 */
	@NotEmpty(message = "The translation text cannot be empty")
	private String chineseText;

	/**
	 * Translate into target language
	 */
	@NotEmpty(message = "The translation target language cannot be empty")
	private List<String> toLanguages;
	
	public AutoTranslateDTO() {
		
	}
	
	public AutoTranslateDTO(String chineseText, String... toLanguages) {
		super();
		this.chineseText = chineseText;
		this.toLanguages = ListUtil.toLinkedList(toLanguages);
	}

	public String getChineseText() {
		return chineseText;
	}

	public void setChineseText(String chineseText) {
		this.chineseText = chineseText;
	}

	public List<String> getToLanguages() {
		return toLanguages;
	}

	public void setToLanguages(List<String> toLanguages) {
		this.toLanguages = toLanguages;
	}


}
