package com.dstz.groovy.script.engine.configure;

import com.dstz.groovy.script.engine.GroovyScriptEngine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ab groovy script configuration
 *
 */
@EnableConfigurationProperties(AbGroovyScriptProperties.class)
@Configuration
public class AbGroovyScriptConfigure {

    private final AbGroovyScriptProperties abGroovyScriptProperties;

    public AbGroovyScriptConfigure(AbGroovyScriptProperties abGroovyScriptProperties) {
        this.abGroovyScriptProperties = abGroovyScriptProperties;
    }

    @Bean
    public GroovyScriptEngine groovyScriptEngine() {
        return new GroovyScriptEngine(abGroovyScriptProperties);
    }
}
