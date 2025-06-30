package com.dstz.base.query;

import com.dstz.base.query.impl.DefaultQueryFieldGroup;

import java.util.List;
import java.util.Map;

/**
 * Query Builder API
 *
 */
public interface AbQueryFilter {
	
	// where sql reserved name
	static final String WHERE_SQL = "whereSql";
	// orderBySql sql reservation name
	static final String ORDERBY_SQL = "orderBySql";
	
	/**
	 * Pagination
	 * @return
	 */
	AbPage getPage();
	
	/**
	 * Generates the SQL for the query, sets it in the query parameters, and returns the query parameters
	 * @return
	 */
	Map<String,Object> generateQuerySql();
	
	AbQueryFilter noPage();
	
	AbQueryFilter addFilter(String fildName,Object value ,ConditionType conditionType);

	// Do not process underscores
	AbQueryFilter addFilterOriginal(String fildName,Object value ,ConditionType conditionType);

	// Specify groupKey
	AbQueryFilter addFilter(String fildName,Object value ,ConditionType conditionType,String groupKey);

	
	AbQueryFilter likeFilter(String fildName,Object value );
	
	AbQueryFilter inFilter(String fildName,Object value );
	
	AbQueryFilter notEqFilter(String fildName,Object value );
	
	AbQueryFilter eqFilter(String fildName,Object value );
	
	AbQueryFilter addParam(String fildName, Object value);
	
	AbQueryFilter clearQuery();

	DefaultQueryFieldGroup getQueryFieldGroup();

	List<QuerySort> getQuerySortList();

	/**
	 * Get the query column name, that is, select xxxx from table
	 *
	 * @return Query column name
	 */
	List<String> getSelectColumnNames();
}
