package com.dstz.sys.rest.controller;


import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.sys.api.constant.RightsObjectConstants;
import com.dstz.sys.rest.model.dto.AuthorizationDTO;
import com.dstz.sys.core.entity.SysAuthorization;
import com.dstz.sys.core.manager.SysAuthorizationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.dstz.base.api.vo.ApiResponse.success;

/**
 * <p>
 * General resource authorization configuration front-end controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.SYS_SERVICE_PREFIX + "/authorization")
public class SysAuthorizationController extends AbCrudController<SysAuthorization> {

    public final SysAuthorizationManager sysAuthorizationManager;

    public SysAuthorizationController(SysAuthorizationManager sysAuthorizationManager) {
        this.sysAuthorizationManager = sysAuthorizationManager;
    }

    /**
     * Save authorization result
     *
     * @param saveDTO Parameter object
     */
    @RequestMapping("saveAuthorization")
    public ApiResponse<String> saveAuthorization(@RequestBody AuthorizationDTO saveDTO) {
        return success(() -> sysAuthorizationManager.createAll(saveDTO));
    }

    /**
     * Get the authorization result for initialization
     *
     * @param dto To get the parameter dto of the authorization object
     */
    @PostMapping("getAuthorizations")
    public ApiResponse<List<SysAuthorization>> getAuthorizations(@RequestBody AuthorizationDTO dto) {
        return success(sysAuthorizationManager.getByTarget(dto.getRightsObject(), dto.getRightsTarget()));
    }

    @Override
    protected String getEntityDesc() {
        return "General resource authorization configuration";
    }
}
