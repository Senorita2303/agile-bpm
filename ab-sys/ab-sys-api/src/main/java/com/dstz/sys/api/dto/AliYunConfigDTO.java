package com.dstz.sys.api.dto;

public class AliYunConfigDTO {
	public String regionId;
	public String sysDomain;
	public String signName;
	public String templateCode;
	public String accessKeyId;
	public String accessSecret;

	public AliYunConfigDTO() {
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getSysDomain() {
		return sysDomain;
	}

	public void setSysDomain(String sysDomain) {
		this.sysDomain = sysDomain;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	@Override
	public String toString() {
		return "AliYunConfigDTO{" + "regionId='" + regionId + '\'' + ", sysDomain='" + sysDomain + '\'' + ", signName='"
				+ signName + '\'' + ", templateCode='" + templateCode + '\'' + ", accessKeyId='" + accessKeyId + '\''
				+ ", accessSecret='" + accessSecret + '\'' + '}';
	}
}
