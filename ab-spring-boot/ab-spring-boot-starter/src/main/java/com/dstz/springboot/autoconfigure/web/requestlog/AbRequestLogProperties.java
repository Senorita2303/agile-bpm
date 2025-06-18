package com.dstz.springboot.autoconfigure.web.requestlog;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * Request log attribute configuration
 *
 */
@ConfigurationProperties(prefix = "ab.request-log")
public class AbRequestLogProperties {

    /**
     * Contains the request header name. If not configured, all headers are included.
     */
    private Set<String> includeHeaderNames;

    /**
     * Masked fields
     */
    private Set<String> sensitiveFields;

    /**
     * Exclude addresses
     */
    private String[] excludeUrls;

    /**
     * Enable Output
     */
    private boolean enableOutput = true;

    public Set<String> getIncludeHeaderNames() {
        return includeHeaderNames;
    }

    public void setIncludeHeaderNames(Set<String> includeHeaderNames) {
        this.includeHeaderNames = includeHeaderNames;
    }

    public Set<String> getSensitiveFields() {
        return sensitiveFields;
    }

    public void setSensitiveFields(Set<String> sensitiveFields) {
        this.sensitiveFields = sensitiveFields;
    }

    public String[] getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(String[] excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public boolean isEnableOutput() {
        return enableOutput;
    }

    public void setEnableOutput(boolean enableOutput) {
        this.enableOutput = enableOutput;
    }
}
