package com.dstz.org.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.events.AbUserEvent;
import com.dstz.base.common.exceptions.BusinessMessage;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.base.query.ConditionType;
import com.dstz.base.query.impl.DefaultAbQueryFilter;
import com.dstz.org.enums.RelationTypeConstant;
import com.dstz.org.core.constant.OrgStatusCode;
import com.dstz.org.core.entity.OrgRelation;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.mapper.OrgRelationMapper;
import com.dstz.org.dto.BatchSaveRelationDTO;
import com.dstz.org.dto.RemoveCheckRelationDTO;
import com.dstz.org.dto.SaveGroupUserRelDTO;
import com.dstz.org.dto.SaveRoleUsersDTO;
import com.dstz.org.vo.OrgRelationUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generic service implementation class
 *
 */
@Service("orgRelationManager")
public class OrgRelationManagerImpl extends AbBaseManagerImpl<OrgRelation> implements OrgRelationManager {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrgRelationMapper orgRelationMapper;


    /**
     * Query Group Users
     *
     * @param filter  Query Parameters
     * @return OrgRelationUserVO List
     */
    @Override
    public PageListDTO<OrgRelationUserVO> queryGroupUser(AbQueryFilter filter) {
        return orgRelationMapper.queryGroupUser(filter);
    }

    /**
     * Set the main organization to modify all sub-organizations
     *
     * @param id Relationship changes to id
     */
    @Override
    public void setMaster(String id) {
        OrgRelation orgRelation = orgRelationMapper.selectById(id);
        if (orgRelation == null || StrUtil.isEmpty(orgRelation.getUserId())) return;

        // Relationship Type
        List<String> relationList = Arrays.asList(RelationTypeConstant.GROUP_USER.getKey(), RelationTypeConstant.POST_USER.getKey());
        // Query all the relationships between users, positions, and organizations, and set them as non-primary versions
        List<OrgRelationUserVO> userGroupRelations = orgRelationMapper.getRelationsByParam(relationList, orgRelation.getUserId(), null, null);

        // set up
        if (CollectionUtil.isNotEmpty(userGroupRelations)) {
            userGroupRelations.forEach(rel -> {
                if (NumberPool.INTEGER_ONE.equals(rel.getIsMaster())) {
                    rel.setIsMaster(NumberPool.INTEGER_ZERO);
                    update(BeanCopierUtils.transformBean(rel, OrgRelation.class));
                }
            });
        }
        // Switch whether to use the main version
        orgRelation.setIsMaster(NumberPool.INTEGER_ZERO.equals(orgRelation.getIsMaster()) ? NumberPool.INTEGER_ONE : NumberPool.INTEGER_ZERO);
        update(orgRelation);
    }

    /**
     * Modify job status
     *
     * @param id  Relationship table id
     */
    @Override
    public String changeStatus(String id) {
        OrgRelation orgRelation = orgRelationMapper.selectById(id);
        if (orgRelation == null) return StrPool.FALSE;

        // Modify status update data
        orgRelation.setStatus(NumberPool.INTEGER_ZERO.equals(orgRelation.getStatus()) ? NumberPool.INTEGER_ONE : NumberPool.INTEGER_ZERO);
        update(orgRelation);

        String userId = orgRelation.getUserId();
        if (StrUtil.isEmpty(userId)) return StrPool.FALSE;
        // The current user has modified his or her information and needs to update the cached data
        if (StrUtil.equals(UserContextUtils.getUserId(), userId)) return StrPool.TRUE;
        return StrPool.FALSE;
    }

    /**
     * Get organizational positions
     *
     * @param groupId Organization ID
     * @return OrgRelationUserVO collection
     */
    @Override
    public List<OrgRelationUserVO> getGroupPost(String groupId) {
        return orgRelationMapper.getGroupPost(groupId);
    }

    /**
     * Save organization position
     *
     * @param saveGroupUserRelDTO  Organization position DTO
     */
    @Override
    public void saveGroupUserRel(SaveGroupUserRelDTO saveGroupUserRelDTO) {
        for (String userId : saveGroupUserRelDTO.getUserIds()) {
            if (StrUtil.isEmpty(userId)) continue;
            // Build OrgRelation
            OrgRelation orgRelation = new OrgRelation(saveGroupUserRelDTO.getGroupId(), userId, null, RelationTypeConstant.GROUP_USER.getKey());

            if (ArrayUtil.isNotEmpty(saveGroupUserRelDTO.getRoleIds())) {
                for (String roleId : saveGroupUserRelDTO.getRoleIds()) {
                    // Set parameters
                    orgRelation.setRoleId(roleId);
                    orgRelation.setType(RelationTypeConstant.POST_USER.getKey());
                    // If it does not exist, create it
                    insertRel(orgRelation);
                }
                continue;
            }
            // If it does not exist, add data
            insertRel(orgRelation);
        }
    }

    /**
     * Save role user
     *
     * @param saveRoleUsersDTO  Role User DTO
     * @return  Add quantity
     */
    @Override
    public int saveRoleUsers(SaveRoleUsersDTO saveRoleUsersDTO) {
        final String roleId = saveRoleUsersDTO.getRoleId();
        int i = 0;
        for (String userId : saveRoleUsersDTO.getUserIds()) {
            OrgRelation orgRelation = new OrgRelation(null, userId, roleId, RelationTypeConstant.USER_ROLE.getKey());
            if (checkRelationIsExist(orgRelation)) {
                log.warn("The relationship was added repeatedly and skipped  {}", JsonUtils.toJSONString(orgRelation));
                continue;
            }
            i++;
            orgRelationMapper.insert(orgRelation);
        }
        return i;
    }

    /**
     * * Deleting a user requires deleting the associated relationship.
     *
     * @param ids Entity ID Set
     * @return Delete the number of users
     */
    @Override
    public int removeByIds(Collection<? extends Serializable> ids) {
        Collection<String> relIds=getUserRelationIds(ids);
        if (CollectionUtil.isEmpty(relIds)){
            return super.removeByIds(ids);
        };
        return super.removeByIds(relIds);
    }

    @Override
    public Collection<String> getUserRelationIds(Collection<? extends Serializable> ids){
        return  orgRelationMapper.getUserRelationIds(ids);
    }


    /**
     * Query group users
     *
     * @param queryFilter Query Filter
     * @return Paginated query results
     */
    @Override
    public PageListDTO<OrgRelationUserVO> queryRoleUser(AbQueryFilter queryFilter) {
        return orgRelationMapper.queryRoleUser(queryFilter);
    }

    /**
     * Verify before deleting a role, organization, or position
     * Delete role Check position, Position personnel, Does the role exist?
     * Delete organization, Check Position, Organization
     * Delete position  Check staff
     *
     * @param removeCheckRelationDTO  Delete Validation DTO
     */
    @Override
    public void beforeRemoveRelCheck(RemoveCheckRelationDTO removeCheckRelationDTO) {
        String groupId = removeCheckRelationDTO.getGroupId();
        String roleId = removeCheckRelationDTO.getRoleId();
        // Query users by relationship
        AbQueryFilter filter = DefaultAbQueryFilter.build();
        filter.getPage().setSearchCount(true);
        // Position inspection: whether the personnel at the position exist
        if (StrUtil.isNotEmpty(groupId))
            filter.addFilter("relation.group_id_", groupId, ConditionType.EQUAL);

        if (StrUtil.isNotEmpty(roleId))
            filter.addFilter("relation.role_id_", roleId, ConditionType.EQUAL);

        if (StrUtil.isNotEmpty(roleId) && StrUtil.isNotEmpty(groupId))
            filter.addFilter("relation.type_", RelationTypeConstant.POST.getKey(), ConditionType.NOT_EQUAL);

        PageListDTO<OrgRelationUserVO> pageListDTO = orgRelationMapper.queryRoleUser(filter);
        if (pageListDTO == null || CollUtil.isEmpty(pageListDTO.getRows())) return;

        StringBuilder sb = new StringBuilder("Please remove the following relationships first:<br>");
        for (OrgRelationUserVO orgRelationUserVO : pageListDTO) {
            getRelationNotes(orgRelationUserVO, sb);
        }
        if (pageListDTO.getTotal() > 10) sb.append("......<br>");

        sb.append(" Total [").append(pageListDTO.getTotal()).append("] items, relationships to be removed");
        throw new BusinessMessage(OrgStatusCode.ORG_RELATION_REMOVE_CHECK.formatDefaultMessage(sb.toString()));
    }

    private void getRelationNotes(OrgRelationUserVO relation, StringBuilder sb) {
        // Position
        if (RelationTypeConstant.POST.getKey().equals(relation.getType())) {
            sb.append("Position: ").append(relation.getPostName());
        } else if (RelationTypeConstant.POST_USER.getKey().equals(relation.getType())) {
            sb.append("Position [").append(relation.getPostName()).append("] Next User：").append(relation.getUserFullname());
        } else if (RelationTypeConstant.GROUP_USER.getKey().equals(relation.getType())) {
            sb.append("Group [").append(relation.getGroupName()).append("] Next User：").append(relation.getUserFullname());
        } else if (RelationTypeConstant.USER_ROLE.getKey().equals(relation.getType())) {
            sb.append("Role [").append(relation.getRoleName()).append("] Next User：").append(relation.getUserFullname());
        }
        sb.append("<br>");
    }

    /**
     * Batch save organization role relationships
     *
     * @param batchSaveRelationDTO  Saving DTOs in batches
     */
    @Override
    public void batchSave(BatchSaveRelationDTO batchSaveRelationDTO) {
        String[] roleIds = batchSaveRelationDTO.getRoleIds().split(StrPool.COMMA);
        String[] groupIds = batchSaveRelationDTO.getGroupIds().split(StrPool.COMMA);
        Arrays.asList(roleIds).forEach(roleId ->
                Arrays.asList(groupIds).forEach(groupId -> {
                    OrgRelation orgRelation = new OrgRelation(groupId, null, roleId, RelationTypeConstant.POST.getKey());
                    insertRel(orgRelation);
                })
        );
    }


    /**
     * Delete a relationship based on organization id
     *
     * @param id   Organization ID
     */
    @Override
    public void removeGroupPostByGroupId(String id) {
        orgRelationMapper.removeGroupPostByGroupId(id);
    }

    /**
     * Get all relationships of a user
     *
     * @param userId  User ID
     * @param relationType  Relationship Type
     * @return  OrgRelationUserVO collection
     */
    @Override
    public List<OrgRelationUserVO> getUserRelation(String userId, String relationType) {
        List<OrgRelationUserVO> orgRelationUserVOS= orgRelationMapper.getUserRelation(userId, relationType);
        if (CollectionUtil.isEmpty(orgRelationUserVOS)){
            return  Collections.emptyList();
        }
        return orgRelationUserVOS;
    }

    /**
     * Check if the role already exists
     *
     * @param orgRelation  Role Relationship
     * @return boolean
     */
    private boolean checkRelationIsExist(OrgRelation orgRelation) {
        return orgRelationMapper.getCountByRelation(orgRelation) > NumberPool.INTEGER_ZERO;
    }

    /**
     * Save associations
     *
     * @param orgRelation  Role Relationship
     */
    private void insertRel(OrgRelation orgRelation) {
        // Create if not exists
        if (! checkRelationIsExist(orgRelation)) {
            orgRelationMapper.insert(orgRelation);
        } else {
            log.warn("The relationship was added repeatedly and skipped.  {}", JsonUtils.toJSONString(orgRelation));
        }
    }

    @Override
    public void removeByUserId(String userId) {
        if (StrUtil.isNotEmpty(userId)){
            orgRelationMapper.removeByUserId(userId);
        }
    }
}

