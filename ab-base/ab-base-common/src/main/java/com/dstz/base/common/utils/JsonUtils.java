package com.dstz.base.common.utils;

import cn.hutool.core.text.replacer.LookupReplacer;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.ApiException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * JSON common operation tool class
 *
 */
public class JsonUtils {

	private static final LookupReplacer ESCAPE_JSON = new LookupReplacer(new String[][]{
			{"\"", "\\\""},
			{"\\", "\\\\"},
			{"/", "\\/"}
	});

	private static final ObjectMapper OBJECT_MAPPER;

	static {
		OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();
		OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
		OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		OBJECT_MAPPER.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
	}

	/**
	 * Convert an object to a JSON string
	 *
	 * @param object
	 *            Object
	 * @return JSON string
	 */
	public static String toJSONString(Object object) {
		try {
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return StrUtil.EMPTY;
		}
	}

	/**
	 * Convert an object to a JSON byte array
	 *
	 * @param object
	 *            Object
	 * @return JSON byte array
	 */
	public static byte[] toJSONBytes(Object object) {
		try {
			return OBJECT_MAPPER.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return new byte[0];
		}
	}

	/**
	 * Convert an object to a JSON string
	 *
	 * @param object
	 *            Object
	 * @param feature
	 *            Serialization features
	 * @return JSON string
	 */
	public static String toJSONString(Object object, SerializationFeature... feature) {
		try {
			ObjectWriter writer = OBJECT_MAPPER.writer();
			return writer.withFeatures(feature).writeValueAsString(object);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Convert an object to a string and specify the date format
	 *
	 * @param object
	 *            Object
	 * @param dateFormat
	 *            Date format
	 * @return JSON string
	 */
	public static String toJSONStringWithDateFormat(Object object, String dateFormat) {
		try {
			ObjectWriter writer = OBJECT_MAPPER.writer();
			return writer.with(new SimpleDateFormat(dateFormat)).writeValueAsString(object);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Parse JSON into a specified type of object
	 *
	 * @param json
	 *            JSON string
	 * @param clazz
	 *            Type
	 * @param <T>
	 *            T
	 * @return Specify the type of object
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		if (StrUtil.isEmpty(json)) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Parse JSON into a specified type of object
	 *
	 * @param jsonBytes
	 *            JSON byte array
	 * @param clazz
	 *            Type
	 * @param <T>
	 *            T
	 * @return Specify the type of object
	 */
	public static <T> T parseObject(byte[] jsonBytes, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(jsonBytes, clazz);
		} catch (IOException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Parse JSON to a specified type reference type object
	 *
	 * @param json
	 *            JSON string
	 * @param typeReference
	 *            Type reference
	 * @param <T>
	 *            T
	 * @return Specify the type of object
	 */
	public static <T> T parseObject(String json, TypeReference<T> typeReference) {
		try {
			return OBJECT_MAPPER.readValue(json, typeReference);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Parse JSON into a specified type of object
	 *
	 * @param jsonBytes
	 *            JSON byte array
	 * @param typeReference
	 *            Type reference
	 * @param <T>
	 *            T
	 * @return Specify the type of object
	 */
	public static <T> T parseObject(byte[] jsonBytes, TypeReference<T> typeReference) {
		try {
			return OBJECT_MAPPER.readValue(jsonBytes, typeReference);
		} catch (IOException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Parse JSON into a list
	 *
	 * @param json
	 *            JSON string
	 * @param clazz
	 *            Type
	 * @param <T>
	 *            T
	 * @return List
	 */
	public static <T> List<T> parseArray(String json, Class<T> clazz) {
		if (StrUtil.isEmpty(json)) {
			return null;
		}
		JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
		try {
			return OBJECT_MAPPER.readValue(json, javaType);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Convert an object to a JsonNode
	 *
	 * @param object
	 *            Object
	 * @return JSON object
	 */
	public static JsonNode toJSONNode(Object object) {
		return OBJECT_MAPPER.valueToTree(object);
	}

	/**
	 * Convert a string to a JsonNode
	 *
	 * @param jsonStr
	 *            json string
	 * @return JSON object
	 */
	public static JsonNode toJSONNode(String jsonStr) {
		try {
			return OBJECT_MAPPER.readTree(jsonStr);
		} catch (JsonProcessingException e) {
			ReflectionUtils.rethrowRuntimeException(e);
			return null;
		}
	}

	/**
	 * Get JsonNode attribute value
	 *
	 * @param name
	 *            Attribute name
	 * @param objectNode
	 *            json node
	 * @return
	 */
	public static String getValueAsString(String name, JsonNode objectNode) {
		String propertyValue = null;
		JsonNode propertyNode = objectNode.get(name);
		if (propertyNode != null && !propertyNode.isNull()) {
			propertyValue = propertyNode.asText();
		}
		return propertyValue;
	}

	public static ObjectNode createObjectNode() {
		return OBJECT_MAPPER.createObjectNode();
	}

	public static ArrayNode createArrayNode() {
		return OBJECT_MAPPER.createArrayNode();
	}

	/**
	 * Parse JSON to a specified type reference type object
	 *
	 * @param json
	 *            JsonNode
	 * @param <T>
	 *            T
	 * @return Specify an object of type
	 */
	public static <T> T parseObject(JsonNode json, Class<T> clazz) {
		return OBJECT_MAPPER.convertValue(json, clazz);
	}

	/**
	 * Parse JSON into a list
	 *
	 * @param json
	 *            JsonNode
	 * @param clazz
	 *            Type
	 * @param <T>
	 *            T
	 * @return List
	 * @throws IOException
	 */
	public static <T> List<T> parseArray(JsonNode json, Class<T> clazz) {
		if (!json.isArray()) {
			throw new ApiException(GlobalApiCodes.PARSE_ERROR.formatDefaultMessage("JSON non-array format"));
		}

		try {
			return OBJECT_MAPPER.readerForListOf(clazz).readValue(json);
		} catch (IOException e) {
			throw new ApiException(GlobalApiCodes.PARSE_ERROR.formatDefaultMessage("JSON Array "), e);
		}
	}

	public static Map toMap(JsonNode json) {
		return JsonUtils.parseObject(json, Map.class);
	}
	
	public static Map toMap(String json) {
		return JsonUtils.parseObject(json, Map.class);
	}

	/**
	 * Translate special strings that appear in JSON
	 *
	 * @param str String
	 * @return Translated string
	 */
	public static CharSequence escapeString(CharSequence str) {
		return ESCAPE_JSON.replace(str);
	}
}
