package com.dstz.org.core.manager;

import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.query.AbQueryFilter;
import com.dstz.org.core.entity.Role;

import java.util.Collection;
import java.util.List;

/**
 * Organization-Job General Business Processing
 *
 */
public interface OrgPostManager {

	/**
	 * Get the job list based on the primary key ID
	 *
	 * @param ids Organization ID Set
	 * @return Job List
	 */
	List<Role> getByIds(Collection<String> ids);

	/**
	 * Search job postings
	 *
	 * @param queryFilter Query Filters
	 * @return Job List
	 */
	PageListDTO<Role> queryPosts(AbQueryFilter queryFilter);
}
