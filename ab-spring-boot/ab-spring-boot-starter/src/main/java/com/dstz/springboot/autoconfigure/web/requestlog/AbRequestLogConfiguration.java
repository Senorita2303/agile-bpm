package com.dstz.springboot.autoconfigure.web.requestlog;

import cn.hutool.core.util.ObjectUtil;
import com.dstz.base.common.requestlog.AbRequestLog;
import com.dstz.base.common.requestlog.AbRequestLogOutput;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Request log configuration
 *
 */
@ConditionalOnClass(AbRequestLog.class)
@EnableConfigurationProperties(AbRequestLogProperties.class)
@Configuration
public class AbRequestLogConfiguration {

    private final AbRequestLogProperties requestLogProperties;

    public AbRequestLogConfiguration(AbRequestLogProperties requestLogProperties) {
        this.requestLogProperties = requestLogProperties;
    }

    @Bean
    public AbRequestLogOutput abRequestLogOutput() {
        return new AbRequestLogOutput(requestLogProperties.getIncludeHeaderNames(), ObjectUtil.defaultIfNull(requestLogProperties.getSensitiveFields(), Collections.emptySet()), requestLogProperties.getExcludeUrls(), requestLogProperties.isEnableOutput());
    }
}
