package com.dstz.base.manager;


import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dstz.base.api.dto.PageListDTO;
import com.dstz.base.api.dto.QueryParamDTO;
import com.dstz.base.entity.IPersistentEntity;
import com.dstz.base.query.AbQueryFilter;

/**
 * AB General business processing, all general business processing should be implemented
 *
 * @param <T> Entity Type
 */
public interface AbBaseManager<T extends IPersistentEntity> {

    /**
     * Batch creation default size
     */
    int DEFAULT_BULK_CRETE_SIZE = 1000;
    
    /**
     * Creating Entity Objects
     *
     * @param entity entity
     * @return Number of rows affected
     */
    int create(T entity);

    /**
     * Update entity object by entity ID
     *
     * @param entity entity
     * @return Number of rows affected
     */
    int update(T entity);

    /**
     * Update the instance object completely by entity ID, and do not judge the entity object fields as empty.
     *
     * @param entity entity
     * @return Number of rows affected
     */
    int updateFullById(T entity);

    /**
     * Update records based on whereEntity conditions
     *
     * @param entity  Entity object (set condition value, can be null)
     * @param wrapper Entity object encapsulation operation class (can be null, the entity inside is used to generate where statement)
     * @return Number of rows affected
     */
    int update(T entity, Wrapper<T> wrapper);

    /**
     * Create or modify. If the entity ID exists, modify it. If it does not exist, create a new record.
     *
     * @param entity entity
     * @return Number of rows affected
     */
    int createOrUpdate(T entity);
    
    /**
     * Paginated List
     *
     * @param queryFilter  Pagination
     * @param  
     * @return Paginated List 
     */
    PageListDTO<T> query(AbQueryFilter queryFilter);

    /**
     * Paginated List
     *
     * @param baseQuery  Paging query conditions
     * @param wrapper Construction conditions
     * @return Paginated List
     */
     PageListDTO<T> query(QueryParamDTO baseQuery, Wrapper<T> wrapper);

    /**
     * Paginated List
     *
     * @param baseQuery  Paging query conditions
     * @param wrapper Construction conditions
     * @param clazz Returned VO or DTO object
     * @return Paginated List
     */
     <V> PageListDTO<V> query(QueryParamDTO baseQuery, Wrapper<T> wrapper, Class<V> clazz);
    /**
     * Get an entity by its ID
     *
     * @param id Entity ID
     * @return Physical Records
     */
    T getById(Serializable id);

    /**
     * Get an entity by its ID set
     *
     * @param ids Entity ID Set
     * @return Physical Records
     */
    List<T> selectByIds(Collection<? extends Serializable> ids);

    /**
     * Query all records based on entity conditions
     *
     * @param queryWrapper Entity object encapsulation operation class (can be null)
     * @return Physical Records
     */
    List<T> selectByWrapper(Wrapper<T> queryWrapper);

    /**
     * Paginated List
     *
     * @param page         Pagination Object
     * @param queryWrapper Entity object encapsulation operation class (can be null)
     * @return Paging Information
     */
    IPage<T> selectByPage(IPage<T> page, Wrapper<T> queryWrapper);

    /**
     * Query the total number of records based on the Wrapper condition
     *
     * @param queryWrapper Entity object encapsulation operation class (can be null)
     * @return Number of records
     */
    Long selectCount(Wrapper<T> queryWrapper);


    /**
     * Delete records based on entity conditions
     *
     * @param wrapper Entity object encapsulation operation class (can be null, the entity inside is used to generate where statement)
     * @return Number of rows affected
     */
    int remove(Wrapper<T> wrapper);

    /**
     * Deleting an object by entity ID
     *
     * @param id Entity ID
     * @return Number of rows affected
     */
    int removeById(Serializable id);

    /**
     * Delete records by entity ID set
     *
     * @param ids Entity ID Set
     */
    int removeByIds(Collection<? extends Serializable> ids);


    /**
     * According to the Wrapper condition, determine whether there is a record
     *
     * @param queryWrapper Entity object encapsulation operation class
     * @return Does the record exist?
     */
    boolean exists(Wrapper<T> queryWrapper);


    /**
     * Find objects with specified conditions
     * @param queryWrapper condition
     * @return Object Entity
     */
     T selectOne(Wrapper<T> queryWrapper);

    /**
     * Get all object collections
     *
     * @return Object Collection
     */
     List<T> list();

    /**
     * Batch creation, please pay attention to the object size
     *
     * @param list List
     * @return Number of rows affected
     */
    default void bulkCreate(Iterable<T> list) {
        bulkCreate(list, DEFAULT_BULK_CRETE_SIZE);
    }

    /**
     * Batch creation, reducing the batch write size for large objects
     *
     * @param list List
     * @param size Batch size
     * @return Number of rows affected
     */
    void bulkCreate(Iterable<T> list, int size);
    
    /**
     * <pre>
     * Most of the business in the ab system obtains objects according to the code
     * ps: Users clearly know that the model they are operating has a code_ field to use
     * </pre>	
     * 
     * @param code
     * @return
     */
	T getByCode(String code);
	
	/**
	 * <pre>
	 * When the business model has a code, perform code uniqueness verification
	 * </pre>
	 * 
	 * @param entity
	 * @param code
	 * @return true: passed verification | false: failed verification
	 */
	boolean checkCode(T entity, String code);
}
