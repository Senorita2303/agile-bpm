package com.dstz.sys.rest.model.vo;

import java.io.Serializable;

/**
 * @Name SysSerialNoDto
 * @description: Serial number Vo
 */
public class SysSerialNoVO implements Serializable {

	private static final long serialVersionUID = -174309595049453931L;
	private String id;

	private String name;

	private String code;

	private String rule;

	private Integer type;

	private Integer noLength;

	private Integer stepLength;

	private String nowDate;

	private Integer initialValue;

	private Integer nowValue;

	/**
	 * New serial number.
	 */
	protected Integer newNowValue = 0;

	/**
	 * Preview the serial number.
	 */
	protected String overViewTenValue = "";

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

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNoLength() {
		return noLength;
	}

	public void setNoLength(Integer noLength) {
		this.noLength = noLength;
	}

	public Integer getStepLength() {
		return stepLength;
	}

	public void setStepLength(Integer stepLength) {
		this.stepLength = stepLength;
	}

	public String getNowDate() {
		return nowDate;
	}

	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}

	public Integer getInitialValue() {
		return initialValue;
	}

	public void setInitialValue(Integer initialValue) {
		this.initialValue = initialValue;
	}

	public Integer getNowValue() {
		return nowValue;
	}

	public void setNowValue(Integer nowValue) {
		this.nowValue = nowValue;
	}

	public Integer getNewNowValue() {
		return newNowValue;
	}

	public void setNewNowValue(Integer newNowValue) {
		this.newNowValue = newNowValue;
	}

	public String getOverViewTenValue() {
		return overViewTenValue;
	}

	public void setOverViewTenValue(String overViewTenValue) {
		this.overViewTenValue = overViewTenValue;
	}
}
