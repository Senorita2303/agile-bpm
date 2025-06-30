package com.dstz.groovy.script.api;

import java.util.Map;

/**
 * Groovy Scripting Engine
 *
 */
public interface IGroovyScriptEngine {
    /**
     * Run the script
     *
     * @param script script
     * @param vars   variable
     * @return Operation Results
     */
    <T> T evaluate(String script, Map<String, Object> vars);
}
