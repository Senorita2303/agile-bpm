package com.dstz.org.vo;

import com.dstz.base.common.valuemap.AbValueMap;
import com.dstz.base.common.valuemap.AbValueMapType;
import com.dstz.org.enums.OrgStatus;

public class ResourceUserVO implements java.io.Serializable {

	/**
	 * id
	 */
	private String id;

	/**
	 * Name
	 */
	private String fullname;

	/**
	 * Account
	 */
	private String account;

	/**
	 * status
	 */
	@AbValueMap(type = AbValueMapType.ENUM, fixClass = OrgStatus.class, matchField = "status", attrMap = {
			@AbValueMap.AttrMap(originName = "desc", targetName = "statusDesc"),
			@AbValueMap.AttrMap(originName = "labelCss", targetName = "statusCss") })
	private Integer status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResourceUserVO{" + "id='" + id + '\'' + ", fullname='" + fullname + '\'' + ", account='" + account
				+ '\'' + ", status=" + status + '}';
	}
}
