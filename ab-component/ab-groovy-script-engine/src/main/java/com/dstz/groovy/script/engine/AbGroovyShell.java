package com.dstz.groovy.script.engine;

import cn.hutool.core.util.ReflectUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AbGroovyShell extends GroovyShell {

    private static final Logger logger = LoggerFactory.getLogger(AbGroovyShell.class);

    private Script script;

    private volatile boolean parsed;

    public AbGroovyShell(Binding binding) {
        super(binding);
    }

    @Override
    public Object evaluate(String scriptText) throws CompilationFailedException {
        if (!parsed) {
            synchronized (this) {
                if (!parsed) {
                    script = parse(scriptText);
                    parsed = true;
                }
            }
        }
        return script.run();
    }

    /**
     * help gc
     */
    public void helpGc() {
        logger.debug("call help gc");
        script = null;
        resetLoadedClasses();
        ReflectUtil.setFieldValue(this, "loader", null);
    }
}
