package com.dstz.groovy.script.api;

/**
 * Groovy script runs abnormally
 *
 */
public class GroovyEngineEvaluateException extends RuntimeException {

    private static final long serialVersionUID = 1704863836362954551L;

    /**
     * Execute Script
     */
    private final String script;

    public GroovyEngineEvaluateException(String message, String script) {
        super(message);
        this.script = script;
    }

    public GroovyEngineEvaluateException(String message, Throwable cause, String script) {
        super(message, cause);
        this.script = script;
    }

    /**
     * Run the script
     *
     * @return script
     */
    public String getScript() {
        return script;
    }
}
