package com.dstz.groovy.script.engine.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Groovy script property configuration
 *
 */
@ConfigurationProperties(prefix = "ab.groovy")
public class AbGroovyScriptProperties {

    /**
     * Compilation Cache
     */
    private Integer compileCache = 20;

    public Integer getCompileCache() {
        return compileCache;
    }

    public void setCompileCache(Integer compileCache) {
        this.compileCache = compileCache;
    }
}
