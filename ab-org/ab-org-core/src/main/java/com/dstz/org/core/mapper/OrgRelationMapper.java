package com.dstz.org.core.mapper;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.OrgRelation;
import com.dstz.org.vo.OrgPostVO;
import com.dstz.org.vo.OrgRelationUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Mapper interface
 * </p>
 *
 */
@Mapper
public interface OrgRelationMapper extends AbBaseMapper<OrgRelation> {


    /**
     * Query Group Users
     *
     * @param queueFile Query Filters
     * @return Pagination Group Users
     */
    PageListDTO<OrgRelationUserVO> queryGroupUser(AbQueryFilter queueFile);


    /**
     * Get the user's relationship
     *
     * @param userId       must
     * @param relationType Not required
     * @return User Relationship Collection
     */
    List<OrgRelationUserVO> getUserRelation(@Param("userId") String userId, @Param("type") String relationType);

    /**
     * Get organizational positions
     *
     * @param groupId Group ID
     * @return Job Collection
     */
    List<OrgRelationUserVO> getGroupPost(String groupId);


    /**
     * Query the relationship list by parameter
     *
     * @param relationTypes Relationship Type
     * @param userId        User ID
     * @param groupId       Group ID
     * @param roleId        Role ID
     * @return User relationship list
     */
    List<OrgRelationUserVO> getRelationsByParam(@Param("relationTypes") List<String> relationTypes, @Param("userId") String userId, @Param("groupId") String groupId, @Param("roleId") String roleId);


    /**
     * @param relation userId,roleId,groupId,relation Existence-Equality Judgment
     * @param relation If exists, it is not a match
     * @return Check if the role already exists
     */
    int getCountByRelation(OrgRelation relation);

    /**
     * Delete all relationships by userId
     *
     * @param userId User id
     */
    void removeByUserId(Serializable userId);


    /**
     * Delete a post under a group
     *
     * @param groupId Group ID
     */
    void removeGroupPostByGroupId(String groupId);

    /**
     * Query user collection
     *
     * @param queryFilter Query Filter
     * @return User Collection
     */
    PageListDTO<OrgRelationUserVO> queryRoleUser(AbQueryFilter queryFilter);

    /**
     * Get positions based on role ID set and organization ID set
     *
     * @param roleIds  Character ID Set
     * @param groupIds Organization ID Set
     * @return position
     */
    List<OrgPostVO> getPostsByRoleIdsAndGroupIds(@Param("roleIds") Collection<String> roleIds, @Param("groupIds") Collection<String> groupIds);


    Collection<String> getUserRelationIds(@Param("ids") Collection<? extends Serializable> ids);

    /**
     * Search job collection
     *
     * @param queryFilter Query parameter filtering
     * @return Paginated List
     */
    PageListDTO<OrgPostVO> queryPosts(AbQueryFilter queryFilter);
}
