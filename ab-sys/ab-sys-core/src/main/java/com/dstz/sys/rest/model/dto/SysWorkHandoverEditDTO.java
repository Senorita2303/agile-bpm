package com.dstz.sys.rest.model.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * @Name SysWorkHandoverEditDTO
 */
public class SysWorkHandoverEditDTO implements Serializable {

	private static final long serialVersionUID = 8974946297588709585L;
	/**
	 * User ID
	 */
	private String userId;

	/**
	 * Receive user ID
	 */
	private Set<String> receiveUserId;

	private String desc;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Set<String> getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Set<String> receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
