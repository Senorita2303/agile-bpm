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
 * JSON 常用操作工具类
 *
 * @author wacxhs
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
	 * 将对象转换为JSON字符串
	 *
	 * @param object
	 *            对象
	 * @return JSON字符串
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
	 * 将对象转换为JSON字节数组
	 *
	 * @param object
	 *            对象
	 * @return JSON字节数组
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
	 * 将对象转换为JSON字符串
	 *
	 * @param object
	 *            对象
	 * @param feature
	 *            序列化特性
	 * @return JSON字符串
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
	 * 将对象转换为字符串并指定日期格式
	 *
	 * @param object
	 *            对象
	 * @param dateFormat
	 *            日期格式
	 * @return JSON字符串
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
	 * 解析JSON为指定类型对象
	 *
	 * @param json
	 *            JSON字符串
	 * @param clazz
	 *            类型
	 * @param <T>
	 *            T
	 * @return 指定类型对象
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
	 * 解析JSON为指定类型对象
	 *
	 * @param jsonBytes
	 *            JSON字节数组
	 * @param clazz
	 *            类型
	 * @param <T>
	 *            T
	 * @return 指定类型对象
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
	 * 解析JSON为指定类型引用类型对象
	 *
	 * @param json
	 *            JSON字符串
	 * @param typeReference
	 *            类型引用
	 * @param <T>
	 *            T
	 * @return 指定类型对象
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
	 * 解析JSON为指定类型对象
	 *
	 * @param jsonBytes
	 *            JSON字节数组
	 * @param typeReference
	 *            类型引用
	 * @param <T>
	 *            T
	 * @return 指定类型对象
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
	 * 解析JSON为列表
	 *
	 * @param json
	 *            JSON字符串
	 * @param clazz
	 *            类型
	 * @param <T>
	 *            T
	 * @return 列表
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
	 * 将对象转换为JsonNode
	 *
	 * @param object
	 *            对象
	 * @return JSON对象
	 */
	public static JsonNode toJSONNode(Object object) {
		return OBJECT_MAPPER.valueToTree(object);
	}

	/**
	 * 将字符串转换为JsonNode
	 *
	 * @param jsonStr
	 *            json字符串
	 * @return JSON对象
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
	 * 获取JsonNode 属性值
	 *
	 * @param name
	 *            属性名
	 * @param objectNode
	 *            json节点
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
	 * 解析JSON为指定类型引用类型对象
	 *
	 * @param json
	 *            JsonNode
	 * @param <T>
	 *            T
	 * @return 指定类型对象
	 */
	public static <T> T parseObject(JsonNode json, Class<T> clazz) {
		return OBJECT_MAPPER.convertValue(json, clazz);
	}

	/**
	 * 解析JSON为列表
	 *
	 * @param json
	 *            JsonNode
	 * @param clazz
	 *            类型
	 * @param <T>
	 *            T
	 * @return 列表
	 * @throws IOException
	 */
	public static <T> List<T> parseArray(JsonNode json, Class<T> clazz) {
		if (!json.isArray()) {
			throw new ApiException(GlobalApiCodes.PARSE_ERROR.formatDefaultMessage("JSON 非 数组形式 "));
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
	 * 对JSON中出现的特殊字符串做转译
	 *
	 * @param str 字符串
	 * @return 转译后字符串
	 */
	public static CharSequence escapeString(CharSequence str) {
		return ESCAPE_JSON.replace(str);
	}
}
