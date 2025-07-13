package com.dstz.base.common.org;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.dstz.org.api.model.IUser;

import java.util.Map;

/**
 * Simple user, used to support temporary settings of threads in the system
 *
 */
public class SimpleUser implements IUser {

	private static final long serialVersionUID = 2166641057456880889L;

	/**
	 * User ID
	 */
	private String userId;

	/**
	 * Username
	 */
	private String username;

	/**
	 * Username
	 */
	private String fullName;

	/**
	 * Attribute
	 */
	private Map<String, Object> attributes;

	@Override
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public <T> T getAttrValue(String attrName, Class<T> tClass) {
		Object attrValue = ReflectUtil.getFieldValue(this, attrName);
		if (attrValue != null) {
			return Convert.convert(tClass, attrValue);
		}
		return MapUtil.get(attributes, attrName, tClass);
	}

	public SimpleUser withUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public SimpleUser withUsername(String username) {
		this.username = username;
		return this;
	}

	public SimpleUser withFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public SimpleUser withAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
		return this;
	}
}
