package com.dstz.base.api.dto;

/**
 * User Data
 *
 */
public class UserDTO implements java.io.Serializable {

	private static final long serialVersionUID = 687720125614093757L;

	/**
	 * User ID
	 */
	private String id;

	/**
	 * Username
	 */
	private String username;

	/**
	 * User Name
	 */
	private String fullName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "id='" + id + '\'' + ", username='" + username + '\'' + ", fullName='" + fullName + '\''
				+ '}';
	}
}
