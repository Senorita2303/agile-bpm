package com.dstz.org.core.manager;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.OrgRelation;
import com.dstz.org.dto.BatchSaveRelationDTO;
import com.dstz.org.dto.RemoveCheckRelationDTO;
import com.dstz.org.dto.SaveGroupUserRelDTO;
import com.dstz.org.dto.SaveRoleUsersDTO;
import com.dstz.org.vo.OrgRelationUserVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * General business
 * </p>
 *
 */
public interface OrgRelationManager extends AbBaseManager<OrgRelation> {


    /**
     * Query Group Users
     *
     * @param queryFilter Query Filters
     * @return Pagination results
     */
    PageListDTO<OrgRelationUserVO> queryGroupUser(AbQueryFilter queryFilter);

    /**
     * Update whether the organizational relationship is the main position
     *
     * @param id Position ID
     */
    void setMaster(String id);

    /**
     * Modify Status
     *
     * @param id Position ID
     */
    String changeStatus(String id);

    /**
     * Get a position in the organization
     *
     * @param groupId Organization ID
     * @return Job Collection
     */
    List<OrgRelationUserVO> getGroupPost(String groupId);

    /**
     * Save the relationship between user and organization
     *
     * @param saveGroupUserRelDTO Save Position DTO
     */
    void saveGroupUserRel(SaveGroupUserRelDTO saveGroupUserRelDTO);


    /**
     * Save the relationship between user and role
     *
     * @param saveRoleUsersDTO Save Role DTO
     * @return Save quantity
     */
    int saveRoleUsers(SaveRoleUsersDTO saveRoleUsersDTO);

    /**
     * Query user collection
     *
     * @param queryFilter Query Filters
     * @return Paginating a collection of users
     */
    PageListDTO<OrgRelationUserVO> queryRoleUser(AbQueryFilter queryFilter);

    /**
     * Verify before deleting
     *
     * @param removeCheckRelationDTO Delete Validation DTO
     */
    void beforeRemoveRelCheck(RemoveCheckRelationDTO removeCheckRelationDTO);

    /**
     * Batch save organization role relationships
     *
     * @param batchSaveRelationDTO Saving DTOs in batches
     */
    void batchSave(BatchSaveRelationDTO batchSaveRelationDTO);

    /**
     * Get all relationships of a user
     *
     * @param userId       User ID
     * @param relationType Relationship Type
     * @return Associated User Collection
     */
    List<OrgRelationUserVO> getUserRelation(String userId, String relationType);

    /**
     * Delete a position in the organization
     *
     * @param groupId Organization ID
     */
    void removeGroupPostByGroupId(String groupId);

    void  removeByUserId(String userId);

    Collection<? extends Serializable>  getUserRelationIds(Collection<? extends Serializable> ids);

}
