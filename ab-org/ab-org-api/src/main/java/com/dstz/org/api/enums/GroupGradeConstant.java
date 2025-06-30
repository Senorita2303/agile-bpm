package com.dstz.org.api.enums;

import java.util.Arrays;

/**
 * Organizational Level
 * 
 */
public enum GroupGradeConstant {

	/**
	 * group
	 */
	GROUP("0", "0-group"),
	/**
	 * company
	 */
	COMPANY("1", "1-company"),

	/**
	 * department
	 */
	DEPARTMENT("3", "3-department"),

	/**
	 * team
	 */
	TEAM("5", "5-team");


	/**
	 * Level KEY
	 */
	private final String key;

	/**
	 * Level Label
	 */
	private final String label;

	GroupGradeConstant(String key, String label) {
		this.key = key;
		this.label = label;
	}

	public static GroupGradeConstant valueOfKey(Integer key) {
		return key == null ? null : valueOfKey(key.toString());
	}

	public static GroupGradeConstant valueOfKey(String key) {
		return Arrays.stream(values()).filter(item -> item.getKey().equals(key)).findFirst().orElse(null);
	}

	public String getKey() {
		return key;
	}

	public String getLabel() {
		return label;
	}
}
