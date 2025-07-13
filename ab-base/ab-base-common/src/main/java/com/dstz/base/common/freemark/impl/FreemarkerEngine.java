package com.dstz.base.common.freemark.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.enums.GlobalApiCodes;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.freemark.IFreemarkScript;
import com.dstz.base.common.freemark.IFreemarkerEngine;
import com.dstz.base.common.property.PropertyEnum;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <pre>
 * Freemarker interface definition
 * </pre>
 */
@Component
public class FreemarkerEngine implements IFreemarkerEngine {
    private Configuration formTemplateConfig;
    protected Logger LOG = LoggerFactory.getLogger(getClass());

    public Configuration getFormTemplateConfiguration() {
        try {
            if (formTemplateConfig == null) {
                String templatePath = PropertyEnum.FORM_TEMPLATE_URL.getPropertyValue(String.class);
                formTemplateConfig = new Configuration();
                formTemplateConfig.setDefaultEncoding("UTF-8");
                formTemplateConfig.setDirectoryForTemplateLoading(new File(templatePath));
            }
            return formTemplateConfig;
        } catch (Exception e) {
            throw new BusinessException(GlobalApiCodes.INTERNAL_ERROR, e);
        }

    }

	@Override
    public String parseByString(String templateSource, Object model) {
        if (model != null && model instanceof Map) {
            // Inject all form generator implementation classes into the template engine
            Map<String, IFreemarkScript> scirptImpls = SpringUtil.getBeansOfType(IFreemarkScript.class);
            for (Entry<String, IFreemarkScript> scriptMap : scirptImpls.entrySet()) {
                ((Map) model).put(scriptMap.getKey(), scriptMap.getValue());
            }
            model = new freemarker.ext.beans.SimpleMapModel((Map)model, null);
        }

        try {
            Configuration cfg = new Configuration();
            StringTemplateLoader loader = new StringTemplateLoader();
            cfg.setTemplateLoader(loader);
            cfg.setClassicCompatible(true);
            loader.putTemplate("freemaker", templateSource);
            Template template = cfg.getTemplate("freemaker");
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            LOG.error(String.format("Freemarker template [%s] parsing failed: %s", templateSource, e.getMessage()));
            throw new BusinessException(GlobalApiCodes.INTERNAL_ERROR.formatMessage("Template parsing failed, possible reasons are: {}", ExceptionUtil.getRootCause(e).getMessage()), ExceptionUtil.getRootCause(e));
        }
    }

}
