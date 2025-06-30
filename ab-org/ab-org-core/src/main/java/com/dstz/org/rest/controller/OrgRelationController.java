package com.dstz.org.rest.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.api.vo.PageListVO;
import com.dstz.base.common.constats.AbAppRestConstant;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.events.AbUserEvent;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.ConditionType;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.base.web.controller.AbCrudController;
import com.dstz.org.enums.RelationTypeConstant;
import com.dstz.org.core.entity.OrgRelation;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.dto.BatchSaveRelationDTO;
import com.dstz.org.dto.RemoveCheckRelationDTO;
import com.dstz.org.dto.SaveGroupUserRelDTO;
import com.dstz.org.dto.SaveRoleUsersDTO;
import com.dstz.org.vo.OrgRelationUserVO;
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
 * Front Controller
 * </p>
 * 
 */
@RestController
@RequestMapping(AbAppRestConstant.ORG_SERVICE_PREFIX + "/orgRelation")
public class OrgRelationController extends AbCrudController<OrgRelation> {

    /**
     * Allow groupId, roleId to be passed as input parameters to the list page
     */
    final Set<String> accessQueryFilters = CollUtil.newHashSet("groupId", "roleId");
    @Autowired
    OrgRelationManager orgRelationManager;

    @Override
    protected String getEntityDesc() {
        return "User organization relationship";
    }

    /**
     * Query Organization User
     *
     * @param queryParamDto Query Parameters
     * @return Organizing User Collections
     */
    @RequestMapping(value = "queryGroupUser")
    public ApiResponse<PageListVO<OrgRelationUserVO>> queryGroupUser(@Valid @RequestBody QueryParamDTO queryParamDto) {
        // Query the relationship between positions and user groups
        AbQueryFilter filter = new DefaultAbQueryFilter(queryParamDto)
                .addFilter("tgroup.id", queryParamDto.getQueryParam().get("groupId"), ConditionType.EQUAL)
                .addFilter("relation.type", RelationTypeConstant.POST.getKey(), ConditionType.NOT_EQUAL);

        return success(orgRelationManager.queryGroupUser(filter));
    }

    /**
     * Setting up the primary organization
     *
     * @param id Position ID
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "setMaster")
    public ApiResponse<String> setMaster(@NotBlank(message = "Parameter cannot be empty") @RequestParam(name = "id") String id) {
        return success(() -> orgRelationManager.setMaster(id));
    }

    /**
     * Modify Status
     *
     * @param id Position ID
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "updateStatus")
    public ApiResponse<String> updateStatus(@NotBlank(message = "Parameter cannot be empty") @RequestParam(name = "id") String id) {
        String str = orgRelationManager.changeStatus(id);
        if (StrUtil.equals(StrPool.TRUE,str)){
            SpringUtil.publishEvent(new AbUserEvent(CollectionUtil.newArrayList(UserContextUtils.getAccount()), AbUserEvent.EventType.UPDATE_USER));
        }
        return success();
    }

    /**
     * Get organizational positions
     *
     * @param groupId Organization ID
     * @return Organization Role Collection
     */
    @RequestMapping(value = "getGroupPost")
    public ApiResponse<List<OrgRelationUserVO>> getGroupPost(@NotBlank(message = "Parameter cannot be empty") @RequestParam(name = "groupId") String groupId) {
        List<OrgRelationUserVO> orgRelations = orgRelationManager.getGroupPost(groupId);
        return success(orgRelations);
    }

    /**
     * Save Organization Post
     *
     * @param saveGroupUserRelDTO Save Position DTO
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "saveGroupUserRel")
    public ApiResponse<String> saveGroupUserRel(@Valid @RequestBody SaveGroupUserRelDTO saveGroupUserRelDTO) {
        return success(() -> orgRelationManager.saveGroupUserRel(saveGroupUserRelDTO));
    }

    /**
     * Save Role User
     *
     * @param saveRoleUsersDTO Save Role DTO
     * @return ApiResponse  Response Object
     */
    @RequestMapping("saveRoleUsers")
    public ApiResponse<String> saveRoleUsers(@Valid @RequestBody SaveRoleUsersDTO saveRoleUsersDTO) {
        String desc = String.format("%dUser roles added successfully", orgRelationManager.saveRoleUsers(saveRoleUsersDTO));
        return success(desc);
    }

    /**
     * Query user collection
     *
     * @param queryParamDto Query Group Users
     * @return User List
     */
    @RequestMapping(value = "roleJson")
    public ApiResponse<PageListVO<OrgRelationUserVO>> roleJson(@Valid @RequestBody QueryParamDTO queryParamDto) {
        // Query the relationship between positions and user groups
        AbQueryFilter filter = new DefaultAbQueryFilter(queryParamDto)
                .addFilter("role.id", queryParamDto.getQueryParam().get("roleId"), ConditionType.EQUAL)
                .addFilter("relation.type", RelationTypeConstant.POST.getKey(), ConditionType.NOT_EQUAL);
        return success(orgRelationManager.queryRoleUser(filter));
    }

    /**
     * Verify before deleting a role, organization, or position
     * Delete role Verify whether the position, position personnel, and role personnel exist
     * Delete organization, verify position, and organization personnel
     * Delete position Verify position personnel
     *
     * @param removeCheckRelationDTO Delete Validation DTO
     * @return ApiResponse  Response Object
     */
    @RequestMapping(value = "removeRelCheck")
    public ApiResponse<String> removeRelCheck(@RequestBody RemoveCheckRelationDTO removeCheckRelationDTO) {
        return success(() -> orgRelationManager.beforeRemoveRelCheck(removeCheckRelationDTO));
    }

    /**
     * Batch set organization role association
     *
     * @param batchSaveRelationDTO Tired of saving DTO
     * @return ApiResponse   Response Object
     */
    @RequestMapping(value = "batchSave")
    public ApiResponse<String> batchSave(@Valid @RequestBody BatchSaveRelationDTO batchSaveRelationDTO) {
        return success(() -> orgRelationManager.batchSave(batchSaveRelationDTO));
    }

    @Override
    public Set<String> getAccessQueryFilters() {
        return accessQueryFilters;
    }
}
