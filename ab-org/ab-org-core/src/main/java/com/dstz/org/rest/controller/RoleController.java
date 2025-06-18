package com.dstz.org.rest.controller;


import cn.hutool.core.collection.CollUtil;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.org.core.entity.Role;
import com.dstz.org.core.manager.RoleManager;
import com.dstz.org.dto.SaveRoleDTO;
import com.dstz.org.vo.ResourceRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

import static com.dstz.base.api.vo.ApiResponse.success;

/**
 * <p>
 * Role Management Front Controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.ORG_SERVICE_PREFIX + "/role")
public class RoleController extends AbCrudController<Role> {


    /**
     * Allow code, enabled, name, typeCode as input parameters to the list page
     */
    final Set<String> accessQueryFilters = CollUtil.newHashSet("code", "enabled", "name", "typeCode");
    @Autowired
    RoleManager roleManager;

    @Override
    protected String getEntityDesc() {
        return "Role";
    }

    @RequestMapping(value = "saveRoleDto")
    public ApiResponse<String> saveRoleDto(@Valid @RequestBody SaveRoleDTO saveRoleDTO) {
        return success(String.format( roleManager.saveRoleDto(saveRoleDTO), getEntityDesc()));
    }

    /**
     * Get role collection based on resources
     *
     * @param resourcesId Resource ID
     * @return Character Collection
     */
    @RequestMapping("getRoleListByResource")
    public ApiResponse<List<ResourceRoleVO>> getRoleListByResource(@RequestParam @NotBlank(message = "参数不能为空") String resourcesId) {
        return success(roleManager.getRoleListByResource(resourcesId));
    }

    @Override
    public Set<String> getAccessQueryFilters() {
        return accessQueryFilters;
    }
}
