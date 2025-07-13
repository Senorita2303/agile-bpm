package com.dstz.auth.model.dto;
/**
 *     WeChat message body
 *
 *
 */
public class WxMsgDTO {
	// OPENID
	private String touser ;
	// For more message types, please refer to the WeChat official documentation. Here, only text messages are sent by default.  https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Service_Center_messages.html
	private String msgtype = "textcard";
	private WxTextContext wxTextContext;
	private WxTextCardContext textcard;
	private Integer agentid = null;
	
	public WxMsgDTO() {
		
	}

	public WxMsgDTO(String openid,String content) {
		WxTextContext wxContent = new WxTextContext(content);
		this.wxTextContext = wxContent;
		this.touser = openid;
	}
	
	public WxMsgDTO(String openid,String title,String description ,String url) {
		WxTextCardContext wxContent = new WxTextCardContext(title,description,url);
		this.textcard = wxContent;
		this.touser = openid;
	}
	
	
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public WxTextContext getWxTextContext() {
		return wxTextContext;
	}

	public void setWxTextContext(WxTextContext wxTextContext) {
		this.wxTextContext = wxTextContext;
	}

	public WxTextCardContext getTextcard() {
		return textcard;
	}

	public void setTextcard(WxTextCardContext textcard) {
		this.textcard = textcard;
	}

	public Integer getAgentid() {
		return agentid;
	}

	public void setAgentid(Integer agentid) {
		this.agentid = agentid;
	}

}
