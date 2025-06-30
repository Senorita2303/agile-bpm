package com.dstz.sys.api.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Name SysDataSourceDefAttribute
 * @description: Data source template properties
 */
public class SysDataSourceDefAttribute implements Serializable {
	private static final long serialVersionUID = -2623523053919830352L;

	/**
	 * Name
	 */
	@NotEmpty
	private String name;
	/**
	 * Description
	 */
	@NotEmpty
	private String comment;
	/**
	 * Parameter type
	 */
	@NotEmpty
	private String type;
	/**
	 * Is it required?
	 */
	private boolean required;
	/**
	 * Default value
	 */
	private String defaultValue;
	/**
	 * Value, this field
	 */
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
