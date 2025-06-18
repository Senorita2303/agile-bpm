package com.dstz.sys.api.constant;

/**
 * @Name HolidayConstants
 * @description: Holiday type enumeration
 */
public enum HolidayTypeConstants {

	WORKDAY("DW", "Working Day"),

	WEEKEND("DR", "Weekend"),

	LEGAL_HOLIDAY("LR", "Legal holidays"),

	LEGAL_WORKDAY("LW", "Legal working day"),

	COMPANY_HOLIDAY("CR", "Company holidays"),

	COMPANY_WORKDAY("CW", "Company working day"),;

	private final String type;
	private final String desc;

	HolidayTypeConstants(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
