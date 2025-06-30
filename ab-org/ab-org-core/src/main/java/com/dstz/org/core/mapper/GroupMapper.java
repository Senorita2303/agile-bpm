package com.dstz.org.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.Group;
import com.dstz.org.vo.GroupUserCountVO;
import com.dstz.org.vo.GroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Organizational Structure Mapper Interface
 * </p>
 * 
 */
@Mapper
public interface GroupMapper extends AbBaseMapper<Group> {

    void removeAll();

    /**
     * @param queryFilter Query Filters
     * @return Group List
     */
    List<GroupVO> queryGroup(AbQueryFilter queryFilter);

    /**
     * Query Group User Interface
     *
     * @return Number of Group Users VO
     */
    List<GroupUserCountVO> getGroupUserCount();

    /**
     * Filter children by path
     * Group service interface adaptation: GroupRelationApiImpl
     *
     * @param path Path
     * @param type Type
     * @return Group List
     */
    List<Group> getChildByPath(@Param("path") String path, @Param("type") String type);

    /**
     * Get the next level of children
     * Group service interface adaptation: GroupRelationApiImpl
     *
     * @param parentId Parent group id
     * @return Group List
     */
    List<Group> getChildrenByParentId(String parentId);


    /**
     * Get the group list based on the user ID, including position, group, and the first one is the main group
     * Group service interface adaptation: AbGroupApiImpl
     *
     * @param page   Pagination
     * @param userId User id
     * @return Group List
     */
    <P extends IPage<Group>> P getByUserId(P page, @Param("userId") String userId);

}
