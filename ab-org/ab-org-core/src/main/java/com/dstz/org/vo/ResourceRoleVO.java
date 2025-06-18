package com.dstz.org.vo;

public class ResourceRoleVO {

	private String id;

	/**
	 * Role Name
	 */
	private String name;

	/**
	 * code
	 */
	private String code;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "ResourceRoleVO{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", code='" + code + '\'' + '}';
	}
}
