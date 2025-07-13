package com.dstz.base.common.enums;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Date;

public enum DataType {
	/**
	 * Floating point
	 */
	DOUBLE("number","Number", Double.class),

	/**
	 * String
	 */
	STRING("varchar","String", String.class),

	/**
	 * Boolean
	 */
	BOOL("bool","Boolean", Boolean.class),

	/**
	 * Date
	 */
	DATE("date","Date", Date.class);


	/**
	 * Type
	 */
	private final String type;

	/**
	 * Describe
	 */
    private final String desc;

	/**
	 * java type
	 */
	@JsonIgnore
	private final Class<?> javaType;

    DataType(String type, String desc, Class<?> javaType) {
        this.type = type;
        this.desc = desc;
	    this.javaType = javaType;
    }
    

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public Class<?> getJavaType() {
		return javaType;
	}

	public static Object parseValueByType(String dataType, Object value) {
		if (ObjectUtil.isEmpty(value)) {
			return null;
		}
		Class<?> convertType = Arrays.stream(values()).filter(obj -> StrUtil.equalsIgnoreCase(obj.getType(), dataType)).findFirst().map(DataType::getJavaType).orElse(null);
		Assert.notNull(convertType, () -> new IllegalArgumentException(String.format("Unable to convert to target type：%s", dataType)));
		return Convert.convert(convertType, value);
	}
}
