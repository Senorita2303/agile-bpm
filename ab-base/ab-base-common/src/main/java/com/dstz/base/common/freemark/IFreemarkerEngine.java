package com.dstz.base.common.freemark;

import java.io.IOException;

import freemarker.template.TemplateException;
/**
 * <pre>
 * Freemarker interface definition
 * </pre>
 */
public interface IFreemarkerEngine {

    /**
     * Parse the content according to the string template
     *
     * @param templateSource String template.
     * @param model          Environmental parameters.
     * @return Parsed text
     * @throws TemplateException
     * @throws IOException
     */
    String parseByString(String templateSource, Object model);

}