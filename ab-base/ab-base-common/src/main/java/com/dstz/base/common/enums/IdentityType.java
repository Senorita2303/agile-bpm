package com.dstz.base.common.enums;

/**
 * Personnel Type
 * More types can be expanded according to the situation. Currently, AB uses user, role, org, and post
 *
 */
public enum IdentityType {
	 USER("user", "User"),
	 ROLE("role", "Role"),
	 ORG("org", "Organization"),
	 GROUP("group", "Group"),
	 POST("post", "Post");

	/**
	 * Key
	 */
	private final String key;
	/**
	 * Value
	 */
	private final String value;

	IdentityType(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return key;
	}

	/**
	 * Get the object by key
	 * 
	 * @param key
	 * @return
	 */
	public static IdentityType fromKey(String key) {
		for (IdentityType c : IdentityType.values()) {
			if (c.getKey().equalsIgnoreCase(key))
				return c;
		}
		throw new IllegalArgumentException(key);
	}
}
