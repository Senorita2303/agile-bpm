package com.dstz.org.core.manager;

import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.manager.AbBaseManager;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.Group;
import com.dstz.org.dto.SaveGroupDTO;
import com.dstz.org.vo.GroupTreeVO;
import com.dstz.org.vo.GroupVO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Organizational structure General business category
 * </p>
 *
 */
public interface GroupManager extends AbBaseManager<Group> {


    /**
     * Organization tree interface
     *
     * @param queryParamDTO Query Condition DTO
     * @return Group List
     */
    List<GroupVO> queryGroup(QueryParamDTO queryParamDTO);

    /**
     * Get group information
     *
     * @param id Group id
     * @return group
     */
    GroupVO getGroupVo(String id);

    /**
     * Save the group
     *
     * @param saveGroupDTO Save Group DTO
     * @return Group id
     */
    String saveGroup(SaveGroupDTO saveGroupDTO);

    /**
     * Get the group tree
     *
     * @return Tree Group List
     */
    List<GroupTreeVO> getOrgTree(AbQueryFilter abQueryFilter);

    /**
     * pass  path Filter children
     * Group service interface adaptation: GroupRelationApiImpl
     *
     * @param path Path
     * @param type Type
     * @return Group List
     */
    List<Group> getChildByPathAndType(String path, Integer type);

    /**
     * Get the next level of children
     * Group service interface adaptation: GroupRelationApiImpl
     *
     * @param parentId Parent group id
     * @return Group List
     */
    List<Group> getChildrenByParentId(String parentId);

    /**
     * Get the list of groups by code
     * Group service interface adaptation: AbGroupApiImpl
     *
     * @param codes Group Code
     * @return Group List
     */
    List<Group> selectByCodes(Collection<String> codes);

    /**
     * Get the group list based on the user ID
     * Group service interface adaptation: AbGroupApiImpl
     *
     * @param userId User id
     * @return Group List
     */
    List<Group> getByUserId(String userId);
}
