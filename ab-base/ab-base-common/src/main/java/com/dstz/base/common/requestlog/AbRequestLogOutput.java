package com.dstz.base.common.requestlog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.dstz.base.common.events.AbRequestLogEvent;
import com.dstz.base.common.jackson.OnlyFilterProvider;
import com.dstz.base.common.jackson.PropertyFilterAdapter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;

/**
 * Request log output
 *
 */
public class AbRequestLogOutput {

	private static final Logger logger = LoggerFactory.getLogger(AbRequestLogOutput.class);

	/**
	 * Contains the name of the header
	 */
	private final Set<String> includeHeaderNames;

	/**
	 * Masked fields
	 */
	private final Set<String> sensitiveFields;

	/**
	 * Ignore Address
	 */
	private final String[] excludeUrls;

	/**
	 * Enable Output
	 */
	private final boolean enableOutput;

	/**
	 * object mapper
	 */
	private final ObjectMapper objectMapper;

	public AbRequestLogOutput(Set<String> includeHeaderNames, Set<String> sensitiveFields, String[] excludeUrls,
			boolean enableOutput) {
		this.includeHeaderNames = includeHeaderNames;
		this.sensitiveFields = sensitiveFields;
		this.excludeUrls = excludeUrls;
		this.enableOutput = enableOutput;
		this.objectMapper = createObjectMapper();
	}

	private ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
		objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN));
		objectMapper.setFilterProvider(new OnlyFilterProvider(new PropertyFilterAdapter() {
			@Override
			public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer)
					throws Exception {
				LogSensitiveField logSensitiveField = writer.getAnnotation(LogSensitiveField.class);
				if (logSensitiveField != null || sensitiveFields.contains(writer.getName())) {
					gen.writeStringField(writer.getName(), "***");
				} else {
					writer.serializeAsField(pojo, gen, prov);
				}
			}
		}));
		// FilterId
		objectMapper.setSerializerFactory(
				objectMapper.getSerializerFactory().withSerializerModifier(new BeanSerializerModifier() {
					@Override
					public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
							JsonSerializer<?> serializer) {
						return serializer.withFilterId(ObjectUtils.NULL);
					}

					@Override
					public JsonSerializer<?> modifyMapSerializer(SerializationConfig config, MapType valueType,
							BeanDescription beanDesc, JsonSerializer<?> serializer) {
						return serializer.withFilterId(ObjectUtils.NULL);
					}
				}));
		return objectMapper;
	}

	@EventListener(AbRequestLogEvent.class)
	public void abRequestLogEventListener(AbRequestLogEvent abRequestLogEvent) {
		// Both output log enable and log INFO must be enabled
		if (!(this.enableOutput && logger.isInfoEnabled())) {
			return;
		}
		final AbRequestLog requestLog = abRequestLogEvent.getRequestLog();
		// exclude url
		if (PatternMatchUtils.simpleMatch(excludeUrls, requestLog.getPathPattern())) {
			return;
		}
		final boolean isResponse = AbRequestLogEvent.EventType.POST_PROCESS.equals(abRequestLogEvent.getEventType());
		StringBuilder append;
		if (isResponse) {
			append = new StringBuilder(StringUtils.center(" response log ", 30, "*"));
		} else {
			append = new StringBuilder(StringUtils.center(" request log ", 30, "*"));
		}
		append.append('\n').append("request url: ").append(requestLog.getUrl());
		append.append('\n').append("path pattern: ").append(requestLog.getPathPattern());
		append.append('\n').append("request method: ").append(requestLog.getRequestMethod());
		// Empty includes all
		if (CollUtil.isEmpty(includeHeaderNames)) {
			append.append('\n').append("request header: ").append(toJSONString(requestLog.getRequestHeaderMap()));
		} else {
			append.append('\n').append("request header: ").append(toJSONString(MapUtil
					.filter(requestLog.getRequestHeaderMap(), entry -> includeHeaderNames.contains(entry.getKey()))));
		}
		append.append('\n').append("request time: ")
				.append(DateFormatUtils.format(requestLog.getRequestTime(), DatePattern.NORM_DATETIME_MS_PATTERN));
		append.append('\n').append("trace id: ").append(requestLog.getTraceId());
		append.append('\n').append("username: ").append(requestLog.getUsername());
		append.append('\n').append("client ip: ").append(requestLog.getClientIp());
		append.append('\n').append("request parameter: ").append(toJSONString(requestLog.getRequestParameterMap()));
		append.append('\n').append("request body: ").append(toJSONString(requestLog.getRequestBody()));

		// response log
		if (isResponse) {
			append.append('\n').append("response body: ").append(toJSONString(requestLog.getResponseBody()));
			append.append('\n').append("response time: ")
					.append(DateFormatUtils.format(requestLog.getResponseTime(), DatePattern.NORM_DATETIME_MS_PATTERN));
			append.append('\n').append("exception: ");
			if (requestLog.getException() != null) {
				append.append(ExceptionUtil.stacktraceToString(requestLog.getException()));
			}
			append.append('\n').append("duration ms: ").append(requestLog.getDurationMs());
		}
		logger.info("\n{}", append);
	}

	private String toJSONString(Object object) {
		if (ObjectUtil.isEmpty(object)) {
			return StrUtil.EMPTY;
		}
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return StrUtil.EMPTY;
	}

}
