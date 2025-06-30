package com.dstz.sys.rest.controller;


import cn.hutool.core.collection.CollUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.groovy.script.api.IGroovyScriptEngine;
import com.dstz.sys.core.entity.SysScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Set;

/**
 * <p>
 * Common scripts front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/script")
public class
SysScriptController extends AbCrudController<SysScript> {

    private static final Logger logger = LoggerFactory.getLogger(SysScriptController.class);

    @Autowired
    private IGroovyScriptEngine groovyScriptEngine;

    @Override
    protected String getEntityDesc() {
        return "Commonly used scripts";
    }
    
    /**
     * Allow name, alias as input parameters to the list page
     */
    private Set<String> accessQueryFilters = CollUtil.newHashSet("name","typeCode");
    
    @Override
	public Set<String> getAccessQueryFilters() {
		return accessQueryFilters;
	}

    /**
     * 执行脚本
     *
     * @param name   Script name
     * @param script Scripts
     * @return Interface response-execution result
     */
    @RequestMapping(value = "executeScript", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Object> executeScript(@RequestParam("key") String name, @RequestParam("script") String script) {
    	checkIsDemoEnvironment();
        Object retVal;
        try {
            // Execution results
            retVal = groovyScriptEngine.evaluate(script, new HashMap<>(0));
        } catch (Exception e) {
            logger.warn("Error in executing script, script name: {}, script: {}", name, script, e);
            retVal = String.format("Error executing script, %s", e.getMessage());
        }
        return ApiResponse.success(retVal);
    }
}
