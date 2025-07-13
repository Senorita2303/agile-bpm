package com.dstz.base.common.requestlog;

import cn.hutool.core.map.MapUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Request log
 *
 */
public class AbRequestLog implements java.io.Serializable {

    private static final long serialVersionUID = -3755749914936860032L;

    /**
     * Original request body
     */
    public static final String REQUEST_BODY_RAW = "requestBodyRaw";
    
    /**
     * trace id
     */
    private String traceId;

    /**
     * Operation User ID
     */
    private String userId;

    /**
     * Operation User Name
     */
    private String username;

    /**
     * Operation User Name
     */
    private String fullName;

    /**
     * Operation Department Name
     */
    private String groupName;

    /**
     * Operation Department Id
     */
    private String groupId;

    /**
     * Client IP
     */
    private String clientIp;

    /**
     * Request method
     */
    private String requestMethod;

    /**
     * Request address
     */
    private String url;

    /**
     * Backend Path
     */
    private String pathPattern;

    /**
     * Request time
     */
    private Date requestTime;

    /**
     * Request Header
     */
    private MultiValueMap<String, String> requestHeaderMap;

    /**
     * Request Parameters
     */
    private Map<String, String> requestParameterMap;

    /**
     * Request Body
     */
    private Object requestBody;

    /**
     * Response Body
     */
    private Object responseBody;

    /**
     * Response time
     */
    private Date responseTime;

    /**
     * Time taken in milliseconds
     */
    private Long durationMs;

    /**
     * Exception information
     */
    private Throwable exception;

    /**
     * Binding properties, used for preceding event property binding
     */
    @JsonIgnore
    @Transient
    private Map<Object, Object> attributeMap;

    /**
     * Signature field in cookie
     */
    private String signature;

    public String getTraceId() {
        return traceId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public MultiValueMap<String, String> getRequestHeaderMap() {
        return requestHeaderMap;
    }

    public Map<String, String> getRequestParameterMap() {
        return requestParameterMap;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public Throwable getException() {
        return exception;
    }

    public String getSignature() {
        return signature;
    }

    /**
     * Binding Properties
     *
     * @param key   Attribute KEY
     * @param value Property Value
     */
    public void bindAttribute(Object key, Object value) {
        if (attributeMap == null) {
            attributeMap = MapUtil.newHashMap();
        }
        attributeMap.put(key, value);
    }

    /**
     * 获取属性值
     *
     * @param key 属性KEY
     * @return 属性值
     */
    public Object getAttribute(Object key) {
        return Objects.isNull(attributeMap) ? null : attributeMap.get(key);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * 实例化Builder
     *
     * @return Builder
     */
    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {

        private final AbRequestLog abRequestLog = new AbRequestLog();

        private Builder() {
        }

        public Builder withTraceId(String traceId) {
            this.abRequestLog.traceId = traceId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.abRequestLog.userId = userId;
            return this;
        }

        public Builder withUsername(String username) {
            this.abRequestLog.username = username;
            return this;
        }

        public Builder withFullName(String fullName) {
            this.abRequestLog.fullName = fullName;
            return this;
        }

        public Builder withGroupId(String groupId) {
            this.abRequestLog.groupId = groupId;
            return this;
        }

        public Builder withGroupName(String groupName) {
            this.abRequestLog.groupName = groupName;
            return this;
        }

        public Builder withClientIp(String clientIp) {
            this.abRequestLog.clientIp = clientIp;
            return this;
        }

        public Builder withRequestMethod(String requestMethod) {
            this.abRequestLog.requestMethod = requestMethod;
            return this;
        }

        public Builder withUrl(String url) {
            this.abRequestLog.url = url;
            return this;
        }


        public Builder withPathPattern(String pathPattern) {
            this.abRequestLog.pathPattern = pathPattern;
            return this;
        }

        public Builder withRequestTime(Date requestTime) {
            this.abRequestLog.requestTime = requestTime;
            return this;
        }

        public Builder withRequestHeaderMap(MultiValueMap<String, String> requestHeaderMap) {
            this.abRequestLog.requestHeaderMap = requestHeaderMap;
            return this;
        }

        public Builder withRequestParameterMap(Map<String, String> requestParameterMap) {
            this.abRequestLog.requestParameterMap = requestParameterMap;
            return this;
        }

        public Builder withRequestBody(Object requestBody) {
            this.abRequestLog.requestBody = requestBody;
            if(requestBody != null){
                // 复制请求体副本
                this.abRequestLog.bindAttribute(REQUEST_BODY_RAW, JsonUtils.parseObject(JsonUtils.toJSONString(requestBody), Object.class));
            }
            return this;
        }

        public Builder withResponseBody(Object responseBody) {
            this.abRequestLog.responseBody = responseBody;
            return this;
        }

        public Builder withResponseTime(Date responseTime) {
            this.abRequestLog.responseTime = responseTime;
            this.abRequestLog.durationMs = (responseTime.getTime() - abRequestLog.getRequestTime().getTime());
            return this;
        }

        public Builder withException(Throwable throwable) {
            this.abRequestLog.exception = throwable;
            return this;
        }
        public Builder withSignature(String signature) {
            this.abRequestLog.signature = signature;
            return this;
        }

        public AbRequestLog build() {
            return abRequestLog;
        }
    }
}
