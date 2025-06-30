package com.dstz.base.common.enums;
/**
 * <pre>
 * Currently based on: 5 types of elementui tags
 * </pre>
 */
public enum LabelCss {
	/**
	 * Blue
	 */
	PRIMARY("","Main color"),
	/**
	 * Green
	 */
	SUCCESS("success","Success"),
	/**
	 * Yellow
	 */
	WARNING("warning","Warn"),
	/**
	 * Danger
	 */
	DANGER("danger","Mistake"),
	/**
	 * Information
	 */
	INFO("info","Information"),
	;
	
	/**
	 * Key
	 */
	private final String key;

	/**
	 * Value
	 */
	private final String value;

	LabelCss(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
