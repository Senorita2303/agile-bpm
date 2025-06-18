package com.dstz.sys.rest.controller;


import cn.hutool.core.util.ObjectUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.core.entity.SysConfiguration;
import com.dstz.sys.core.manager.SysConfigurationManager;
import com.dstz.sys.rest.model.vo.SysConfigurationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.dstz.base.api.vo.ApiResponse.success;

/**
 * <p>
 * Front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/sysConfiguration")
public class SysConfigurationController extends AbCrudController<SysConfiguration> {

    @Override
    protected String getEntityDesc() {
        return "";
    }

    @Autowired
    SysConfigurationManager sysConfigurationManager;

    /**
     * Get system configuration based on the code
     * @param code Configuration code
     * @return Configuration information
     */
    @RequestMapping({"/getConfByCode/{code}"})
    public ApiResponse<String> getConfByCode(@PathVariable String code) {
        return ApiResponse.success(ObjectUtil.isNotNull(sysConfigurationManager.getConfByCode(code))?sysConfigurationManager.getConfByCode(code).getJson():"");
    }

    /**
     * Get the system configuration object according to the code
     * @param code Configuration code
     * @return Configuration information
     */
    @GetMapping({"/getConfObjByCode/{code}"})
    public ApiResponse<SysConfiguration> getConfObjByCode(@PathVariable String code) {
        return ApiResponse.success(sysConfigurationManager.getConfByCode(code));
    }

    /**
     * Save entity data
     *
     * @param sysConfiguration Entity
     * @return Interface response-entity ID
     */
    @Override
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public ApiResponse<String> save(@Valid @RequestBody SysConfiguration sysConfiguration) {
        invokeBeforeCheck("saveCheck", sysConfiguration);
        sysConfigurationManager.save(sysConfiguration);
        return ApiResponse.success(sysConfiguration.getId());
    }

    /**
     * Get system property collection
     */
    @RequestMapping(value = "/getSysProperties")
    public ApiResponse<List<SysConfigurationVO>> getSysProperties(@Valid @RequestParam  String codes) {
        return ApiResponse.success(sysConfigurationManager.getSysProperties(codes));
    }

    /**
     * Get system configuration based on the code
     * @param code Configuration code
     * @return Configuration information
     */
    @RequestMapping(value = "/changeEnableByCode")
    public ApiResponse<String> changeEnableByCode(@Valid @RequestParam  String code) {
      sysConfigurationManager.changeEnableByCode(code);
      return success();
    }
}
