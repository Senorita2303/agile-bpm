package com.dstz.org.rest.controller;


import cn.hutool.core.collection.CollUtil;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.utils.BeanConversionUtils;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.org.core.entity.Group;
import com.dstz.org.core.manager.GroupManager;
import com.dstz.org.dto.SaveGroupDTO;
import com.dstz.org.vo.GroupTreeVO;
import com.dstz.org.vo.GroupVO;
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
 * Organizational Structure Front Controller
 * </p>
 *
 */
@RestController
@RequestMapping(AbAppRestConstant.ORG_SERVICE_PREFIX + "/group")
public class GroupController extends AbCrudController<Group> {

    /**
     * Allow fullname, account, roleId as input parameters to the list page
     */
    final Set<String> accessQueryFilters = CollUtil.newHashSet("fullname", "account", "roleId", "id", "groupId");
    @Autowired
    GroupManager groupManager;

    @Override
    public Set<String> getAccessQueryFilters() {
        return accessQueryFilters;
    }

    /**
     * 组织架构明细页面
     *
     * @param id Organization ID
     * @return organization
     */
    @RequestMapping(value = "getGroupVo")
    public ApiResponse<GroupVO> getGroupVo(@NotBlank(message = "Parameter cannot be empty") @RequestParam String id) {
        return success(groupManager.getGroupVo(id));
    }

    /**
     * Get the organization tree
     *
     * @return Tree Organization Collection
     */
    @RequestMapping(value = "getOrgTree")
    public ApiResponse<List<GroupTreeVO>> getOrgTree(@RequestBody QueryParamDTO paramDTO) {
        DefaultAbQueryFilter abQueryFilter = new DefaultAbQueryFilter(paramDTO);
        abQueryFilter.noPage();
        return success(groupManager.getOrgTree(abQueryFilter));
    }

    /**
     * Save Organization
     *
     * @param saveGroupDTO Save Organization DTO
     * @return ApiResponse  Response results
     */
    @RequestMapping(value = "saveGroup")
    public ApiResponse<String> saveGroup(@Valid @RequestBody SaveGroupDTO saveGroupDTO) {
        return success(groupManager.saveGroup(saveGroupDTO));
    }

    @Override
    protected String getEntityDesc() {
        return "Organization";
    }

}
