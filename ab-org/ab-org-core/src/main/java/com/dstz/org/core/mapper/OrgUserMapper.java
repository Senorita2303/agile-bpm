package com.dstz.org.core.mapper;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.OrgUser;
import com.dstz.org.vo.OrgUserListJsonVO;
import com.dstz.org.vo.ResourceUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User Table Mapper Interface
 *
 */
@Mapper
public interface OrgUserMapper extends AbBaseMapper<OrgUser> {


    /**
     * Determine if the user exists。
     */
    Integer isUserExist(OrgUser user);

    /**
     * Get a list of users by role ID
     *
     * @param relId Role ID
     * @param type  Relationship Type
     * @return User List
     */
    List<OrgUser> getUserListByRelation(@Param("relationId") String relId, @Param("relationType") String type);

    /**
     * Get all users under a position
     *
     * @param roleId  Role ID
     * @param groupId Group ID
     * @return User List
     */
    List<OrgUser> getUserListByPost(@Param("roleId") String roleId, @Param("groupId") String groupId);

    /**
     * Get all users under the specified organization path
     *
     * @param path path
     * @return User Collection
     */
    List<OrgUser> getUserListByGroupPath(@Param("path") String path);

    /**
     * Selectively update records based on primary key
     *
     * @param record code
     * @return Delete quantity
     */
    int updateByPrimaryKeySelective(OrgUser record);

    /**
     * User rights query interface
     *
     * @param queryFilter Query Filters
     * @return Permission user collection
     */
    PageListDTO<ResourceUserVO> getUserByResource(AbQueryFilter queryFilter);

    /**
     * User rights query interface
     *
     * @param queryFilter Query Filters
     * @return Paginating a collection of users
     */
    PageListDTO<OrgUser> queryUser(AbQueryFilter queryFilter);

    /**
     * queryFilter Paginated list query
     *
     * @param abQueryFilter
     * @return
     */
    PageListDTO<OrgUserListJsonVO> queryUserList(AbQueryFilter abQueryFilter);

}
