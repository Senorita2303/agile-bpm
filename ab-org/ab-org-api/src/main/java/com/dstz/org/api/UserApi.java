package com.dstz.org.api;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.org.api.model.IUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * User Interface
 *
 */
public interface UserApi {

	/**
	 * Get user by user ID
	 *
	 * @param userId User ID
	 * @return User
	 */
	default IUser getByUserId(String userId) {
		Iterator<? extends IUser> iterator = getByUserIds(Collections.singleton(userId));
		return Objects.nonNull(iterator) && iterator.hasNext() ? iterator.next() : null;
	}

	/**
	 * Get user records based on user ID set
	 *
	 * @param userIds User record set
	 * @return User records
	 */
	Iterator<? extends IUser> getByUserIds(Collection<String> userIds);

	/**
	 * Get user by username
	 *
	 * @param username Username
	 * @return User
	 */
	default IUser getByUsername(String username) {
		Iterator<? extends IUser> iterator = getByUsernames(Collections.singleton(username));
		return Objects.nonNull(iterator) && iterator.hasNext() ? iterator.next() : null;
	}

	/**
	 * Get user list from username collection
	 *
	 * @param usernames Username set
	 * @return User list
	 */
	Iterator<? extends IUser> getByUsernames(Collection<String> usernames);

	/**
	 * Get user records based on group type and group ID set
	 *
	 * @param groupType Group Type
	 * @param groupIds  Group ID
	 * @return User records
	 */
	Iterator<? extends IUser> getByGroupTypeAndGroupIds(String groupType, Collection<String> groupIds);

	/**
	 * User query filtering, as a custom frame protection user chooses to abandon the data loading source
	 *
	 * @param queryParamDTO Query parameter filter object
	 * @return User records
	 */
	default PageListDTO<? extends IUser> queryFilter(QueryParamDTO queryParamDTO) {
		return null;
	}

}
