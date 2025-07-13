package com.dstz.sys.rest.model.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Name ParseStrByFreeMarkerDTO
 * @description: Front-end formatted string request DTO
 */
public class ParseStrByFreeMarkerDTO implements Serializable {
	private static final long serialVersionUID = 6777364995760673690L;

	@NotNull(message = "The string cannot be empty")
	private String str;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return "ParseStrByFreeMarkerDTO{" + "str='" + str + '\'' + '}';
	}
}
