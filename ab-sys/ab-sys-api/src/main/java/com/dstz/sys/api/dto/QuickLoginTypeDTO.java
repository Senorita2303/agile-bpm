package com.dstz.sys.api.dto;

public class QuickLoginTypeDTO {
	public String appLoginType;
	public String qywx;
	public String pcLoginType;
	public String dingTalk;

	public QuickLoginTypeDTO() {
	}

	public String getAppLoginType() {
		return appLoginType;
	}

	public void setAppLoginType(String appLoginType) {
		this.appLoginType = appLoginType;
	}

	public String getQywx() {
		return qywx;
	}

	public void setQywx(String qywx) {
		this.qywx = qywx;
	}

	public String getPcLoginType() {
		return pcLoginType;
	}

	public void setPcLoginType(String pcLoginType) {
		this.pcLoginType = pcLoginType;
	}

	public String getDingTalk() {
		return dingTalk;
	}

	public void setDingTalk(String dingTalk) {
		this.dingTalk = dingTalk;
	}

	@Override
	public String toString() {
		return "QuickLoginTypeDTO{" + "appLoginType='" + appLoginType + '\'' + ", qywx='" + qywx + '\''
				+ ", pcLoginType='" + pcLoginType + '\'' + ", dingTalk='" + dingTalk + '\'' + '}';
	}
}
