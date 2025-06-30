package com.dstz.springboot.autoconfigure.cache.enums;

import com.dstz.component.j2cache.serialization.JacksonJsonSerializer;

/**
 * J2Cache serialization enumeration
 *
 */
public enum J2CacheSerializationEnum {

	/**
	 * JAVA built-in serialization
	 */
	JAVA("java"),

	/**
	 * JSON Serialization
	 */
	JSON(JacksonJsonSerializer.class.getName()),

	/**
	 * FST Serialization
	 */
	FST("fst");

	private final String serializer;

	J2CacheSerializationEnum(String serializer) {
		this.serializer = serializer;
	}

	public String getSerializer() {
		return serializer;
	}
}
