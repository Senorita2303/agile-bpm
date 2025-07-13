package com.dstz.org.api;

import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Group Interface
 *
 */
public interface GroupApi {

	/**
	 * Get group records based on group type and group ID
	 *
	 * @param groupType Group Type
	 * @param groupId   Group ID Set
	 * @return Group Records
	 */
	default IGroup getByGroupId(String groupType, String groupId) {
		Iterator<? extends IGroup> iterator = getByGroupIds(groupType, Collections.singleton(groupId));
		return Objects.nonNull(iterator) && iterator.hasNext() ? iterator.next() : null;
	}

	/**
	 * Get group records based on group type and group ID
	 *
	 * @param groupType Group Type
	 * @param groupIds  Group ID Set
	 * @return Group Records
	 */
	Iterator<? extends IGroup> getByGroupIds(String groupType, Collection<String> groupIds);

	/**
	 * Get a group by group type and group code
	 *
	 * @param groupType Group Type
	 * @param groupCode Group Code
	 * @return Group Records
	 */
	default IGroup getByGroupCode(String groupType, String groupCode) {
		Iterator<? extends IGroup> iterator = getByGroupCodes(groupType, Collections.singleton(groupCode));
		return Objects.nonNull(iterator) && iterator.hasNext() ? iterator.next() : null;
	}

	/**
	 * Get group records by group type and group code
	 *
	 * @param groupType  Group Type
	 * @param groupCodes Group Code
	 * @return Group Records
	 */
	Iterator<? extends IGroup> getByGroupCodes(String groupType, Collection<String> groupCodes);

	/**
	 * Get the associated group based on the user ID
	 *
	 * @param userId User ID
	 * @return Link Group
	 */
	default List<? extends IGroup> getByUserId(String userId) {
		return Arrays.stream(GroupType.values()).map(o -> getByGroupTypeAndUserId(o.getType(), userId)).flatMap(Collection::stream).collect(Collectors.toList());
	}

	/**
	 * Get a list of group records based on group type and user ID
	 *
	 * @param groupType Group Type
	 * @param userId    User Number
	 * @return Group Records
	 */
	List<? extends IGroup> getByGroupTypeAndUserId(String groupType, String userId);

	/**
	 * Group record query filtering
	 *
	 * @param groupType     Group Type
	 * @param queryParamDTO Filter parameters
	 * @return Group Records
	 */
	default List<? extends IGroup> queryFilter(String groupType, QueryParamDTO queryParamDTO) {
		return null;
	}
}
