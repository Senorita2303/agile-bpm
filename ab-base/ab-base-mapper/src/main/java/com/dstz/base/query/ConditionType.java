package com.dstz.base.query;

import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.ApiException;

public enum ConditionType {
	/**
	 * equal
	 */
	EQUAL("EQ", "=", "Equal", new String[] { "varchar", "number", "date", "clob" }),
	/**
	 * Equals to ignore case
	 */
	/*	EQUAL_IGNORE_CASE("EIC", "=", "Equals to ignore case", new String[] { "varchar" }),*/
	/**
	 * Less than
	 */
	LESS("LT", "<", "Less than", new String[] { "number", "date" }),
	/**
	 * Greater than
	 */
	GREAT("GT", ">", "Greater than", new String[] { "number", "date" }),
	/**
	 * Less than or equal to
	 */
	LESS_EQUAL("LE", "<=", "Less than or equal to", new String[] { "number", "date" }),
	/**
	 * Greater than or equal to
	 */
	GREAT_EQUAL("GE", ">=", "Greater than or equal to", new String[] { "number", "date" }),
	/**
	 * Not equal to
	 */
	NOT_EQUAL("NE", "!=", "Not equal", new String[] { "varchar", "number", "date", "clob" }),
	/**
	 * like
	 */
	LIKE("LK", "like", "Similar", new String[] { "varchar", "clob" }),
	/**
	 * Right Similar
	 */
	LEFT_LIKE("LFK", "llike", "Right Similar", new String[] { "varchar", "clob" }),
	/**
	 * Left Similar
	 */
	RIGHT_LIKE("RHK", "rlike", "Left Similar", new String[] { "varchar", "clob" }),
	/**
	 *
	 */
	IS_NULL("INL", "is null", "Empty", new String[] { "varchar", "number", "date", "clob" }, false),
	/**
	 *
	 */
	NOTNULL("NNL", "is not null", "Not empty", new String[] { "varchar", "number", "date", "clob" }, false),
	/**
	 * In...
	 */
	IN("IN", "in", "In", new String[] { "varchar", "number", "date" }),
	/**
	 * Not in
	 */
	NOT_IN("NI", "not in", "Not in", new String[] { "varchar", "number", "date" }),
	/**
	 * Between
	 */
	BETWEEN("BT", ">=,<=", "Between", new String[] { "number", "date" });

	private String key;
	private String condition;
	private String desc;
	private String[] supports;
	/**
	 * Are parameters required?
	 */
	private boolean needParam = true;

	private ConditionType(String key, String condition, String desc, String[] supports) {
		this.key = key;
		this.condition = condition;
		this.desc = desc;
		this.supports = supports;
	}

	private ConditionType(String key, String condition, String desc, String[] supports, boolean needParam) {
		this(key, condition, desc, supports);
		this.needParam = needParam;
	}

	public static ConditionType formKey(String key) {
		for (ConditionType condtion : ConditionType.values()) {
			if (condtion.key.equals(key))
				return condtion;
		}

		throw new ApiException(GlobalApiCodes.PARAMETER_INVALID.formatMessage("入参条件类型不合法：{}", key));
	}

	public String key() {
		return key;
	}

	public String condition() {
		return condition;
	}

	public String desc() {
		return desc;
	}

	public String[] supports() {
		return supports;
	}

	public boolean needParam() {
		return needParam;
	}
}
