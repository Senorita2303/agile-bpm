package com.dstz.base.common.utils;

import java.io.IOException;

import com.dstz.base.common.freemark.IFreemarkerEngine;
import com.dstz.base.common.freemark.impl.FreemarkerEngine;

import cn.hutool.extra.spring.SpringUtil;
import freemarker.template.TemplateException;

/**
 * Freemarker Tools
 *
 */
public class AbFreemarkUtil {

    private static volatile IFreemarkerEngine freemarkerEngine;

    public static IFreemarkerEngine getFreemarkerEngine() {
        if (freemarkerEngine == null) {
            synchronized (AbFreemarkUtil.class) {
                freemarkerEngine = SpringUtil.getBean(FreemarkerEngine.class);
            }
        }
        return freemarkerEngine;
    }

    /**
     * Parse the content according to the string template
     *
     * @param templateSource String template.
     * @param model          Environmental parameters.
     * @return Parsed text
     * @throws TemplateException
     * @throws IOException
     */
    public static String parseByString(String templateSource, Object model) {
        return getFreemarkerEngine().parseByString(templateSource, model);
    }

}

