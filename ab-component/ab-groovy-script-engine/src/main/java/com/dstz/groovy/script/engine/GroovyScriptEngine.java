package com.dstz.groovy.script.engine;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.escape.Html4Unescape;
import cn.hutool.core.text.replacer.StrReplacer;
import cn.hutool.core.util.ObjectUtil;
import com.dstz.groovy.script.api.GroovyEngineEvaluateException;
import com.dstz.groovy.script.api.IGroovyScriptEngine;
import com.dstz.groovy.script.api.IScript;
import com.dstz.groovy.script.engine.configure.AbGroovyScriptProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import groovy.lang.GroovyShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * The scripting engine is used to execute groovy scripts.<br/>
 * A class that implements the IScript interface. It can be used in scripts.
 *
 */
public class GroovyScriptEngine implements IGroovyScriptEngine, ApplicationListener<ContextRefreshedEvent>, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GroovyBinding groovyBinding = new GroovyBinding();

    private final StrReplacer stringEscape = new Html4Unescape();

    private Cache<String, AbGroovyShell> groovyShellCache;

    private final AbGroovyScriptProperties abGroovyScriptProperties;

    public GroovyScriptEngine(AbGroovyScriptProperties abGroovyScriptProperties) {
        this.abGroovyScriptProperties = abGroovyScriptProperties;
    }

    @Override
    public void afterPropertiesSet() {
        groovyShellCache = CacheBuilder.newBuilder()
                .maximumSize(abGroovyScriptProperties.getCompileCache())
                .removalListener((RemovalListener<String, AbGroovyShell>) event -> Optional.ofNullable(event.getValue()).ifPresent(AbGroovyShell::helpGc))
                .build();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T evaluate(String script, Map<String, Object> vars) {
        if (CharSequenceUtil.isBlank(script)) {
            return null;
        }
        script = stringEscape.replace(script).toString();
        if (logger.isDebugEnabled()) {
            logger.debug("implement:{}", script);
            logger.debug("variables:{}", ObjectUtil.toString(vars));
        }
        try {
            groovyBinding.setThreadVariables(vars);
            GroovyShell shell = groovyShellCache.get(DigestUtils.md5DigestAsHex(script.getBytes(StandardCharsets.UTF_8)), () -> new AbGroovyShell(groovyBinding));
            T result = (T)shell.evaluate(script);
            if (logger.isDebugEnabled()) {
                logger.debug("result:{}", result);
            }
            return result;
        } catch (ExecutionException ex) {
            throw new GroovyEngineEvaluateException(ex.getMessage(), ex, script);
        }finally {
            groovyBinding.clearVariables();
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            // Loading an IScript instance
            event.getApplicationContext().getBeansOfType(IScript.class).forEach(groovyBinding::setProperty);
        }
    }
}
