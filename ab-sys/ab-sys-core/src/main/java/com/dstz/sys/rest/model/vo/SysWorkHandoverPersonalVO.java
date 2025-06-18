package com.dstz.sys.rest.model.vo;

import com.dstz.org.api.model.IUser;

import java.io.Serializable;
import java.util.List;

/**
 * @Name SysWorkHandoverPersonalVO
 * @description: TODO
 */
public class SysWorkHandoverPersonalVO implements Serializable {
	private static final long serialVersionUID = -128902638543933299L;
	private String userId;

	private String userAccount;

	private String userFullName;

	private List<SysWorkHandoverPersonalVO> handoverUsers;

	private List<SysWorkHandoverPersonalVO> receiveUsers;

	/**
	 * new instance
	 *
	 * @param user user
	 * @return SysWorkHandoverPersonalVO
	 */
	public static SysWorkHandoverPersonalVO newInstance(IUser user) {
		SysWorkHandoverPersonalVO o = new SysWorkHandoverPersonalVO();
		if (user != null) {
			o.userId = user.getUserId();
			o.userAccount = user.getUsername();
			o.userFullName = user.getFullName();
		}
		return o;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public List<SysWorkHandoverPersonalVO> getHandoverUsers() {
		return handoverUsers;
	}

	public void setHandoverUsers(List<SysWorkHandoverPersonalVO> handoverUsers) {
		this.handoverUsers = handoverUsers;
	}

	public List<SysWorkHandoverPersonalVO> getReceiveUsers() {
		return receiveUsers;
	}

	public void setReceiveUsers(List<SysWorkHandoverPersonalVO> receiveUsers) {
		this.receiveUsers = receiveUsers;
	}
}
