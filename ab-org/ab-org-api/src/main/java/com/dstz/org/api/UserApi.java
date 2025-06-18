package com.dstz.org.api;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.org.api.model.IUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * 用户接口
 *
 * @author wacxhs
 */
public interface UserApi {

	/**
	 * 根据用户ID获取用户
	 *
	 * @param userId 用户ID
	 * @return 用户
	 */
	default IUser getByUserId(String userId) {
		Iterator<? extends IUser> iterator = getByUserIds(Collections.singleton(userId));
		return Objects.nonNull(iterator) && iterator.hasNext() ? iterator.next() : null;
	}

	/**
	 * 根据用户ID集获取用户记录
	 *
	 * @param userIds 用户记录集
	 * @return 用户记录
	 */
	Iterator<? extends IUser> getByUserIds(Collection<String> userIds);

	/**
	 * 根据用户名获取用户
	 *
	 * @param username 用户名
	 * @return 用户
	 */
	default IUser getByUsername(String username) {
		Iterator<? extends IUser> iterator = getByUsernames(Collections.singleton(username));
		return Objects.nonNull(iterator) && iterator.hasNext() ? iterator.next() : null;
	}

	/**
	 * 用户名集合获取用户列表
	 *
	 * @param usernames 用户名集
	 * @return 用户列表
	 */
	Iterator<? extends IUser> getByUsernames(Collection<String> usernames);

	/**
	 * 根据组类型和组ID集获取用户记录
	 *
	 * @param groupType 组类型
	 * @param groupIds  组ID
	 * @return 用户记录
	 */
	Iterator<? extends IUser> getByGroupTypeAndGroupIds(String groupType, Collection<String> groupIds);

	/**
	 * 用户查询过滤，做为自定义对护框用户选择弃数据加载来源
	 *
	 * @param queryParamDTO 查询参数过滤对象
	 * @return 用户记录
	 */
	default PageListDTO<? extends IUser> queryFilter(QueryParamDTO queryParamDTO) {
		return null;
	}




}
