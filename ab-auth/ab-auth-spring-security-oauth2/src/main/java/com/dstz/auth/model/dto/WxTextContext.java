package com.dstz.auth.model.dto;
/**
 * WeChat message text dto
 * 
 */
public class WxTextContext {
	
	private String content;

	public WxTextContext(String content2) {
		this.content = content2;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
