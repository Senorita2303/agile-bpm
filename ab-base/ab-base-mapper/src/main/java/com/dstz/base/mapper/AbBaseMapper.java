package com.dstz.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.entity.IPersistentEntity;
import com.dstz.base.query.AbQueryFilter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

/**
 * ab General Mapper, all Mapper underlying Mapper interface implementation
 *
 * @param <T> Entity Model
 */
public interface AbBaseMapper<T extends IPersistentEntity> extends BaseMapper<T> {
	
	/**
	 * queryFilter Paginated list query
	 * @param page
	 * @return
	 */
	PageListDTO<T> query(AbQueryFilter abQueryFilter);


	/**
	 * Update based on entity ID integrity, without checking if the field is empty
	 *
	 * @param entity entity
	 * @return Number of rows affected
	 */
	int updateFullById(@Param(Constants.ENTITY) T entity);

	/**
	 * Query cursor according to conditions
	 *
	 * @param wrapper Query Condition Package
	 * @return cursor
	 */
	Cursor<T> queryCursorByWrapper(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

	/**
	 * Create records in batches
	 *
	 * @param entityColl Entity Collection
	 * @return Number of rows affected
	 */
	int bulkCreate(@Param(Constants.COLL) Iterable<T> entityColl);
}
