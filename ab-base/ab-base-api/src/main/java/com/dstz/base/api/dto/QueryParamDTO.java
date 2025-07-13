package com.dstz.base.api.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Query condition input
 *
 */
public class QueryParamDTO {

	/**
	 * Number of items displayed per page, default 10
	 */
	@Max(1000)
	@Min(1)
	protected long pageSize = 10;

	/**
	 * Current Page
	 */
	protected long currentPage = 1;

	/**
	 * Sort Field
	 */
	private String sortColumn;

	/**
	 * Order DESC ASC
	 */
	private String sortOrder;

	/**
	 * Whether to query CountSQL
	 */
	private Boolean searchCount = true;

	/**
	 * Whether to enable paging
	 */
	private Boolean enablePage = true;

	/**
	 * Whether to ignore the filter conditions (eliminate the conditions configured in the dialog box itself and only filter the parameter conditions passed in)
	 */
	private Boolean ignoreCondition = false;
	/**
	 * The default type used by bootstrap
	 */
	private Integer offset;

	@Max(1000)
	@Min(1)
	private Integer limit;

	/**
	 * Column Fields
	 */
	private Set<String> columnNames;

	private Map<String, Object> queryParam = new HashMap<>();

	public Map<String, Object> getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(Map<String, Object> queryParam) {
		this.queryParam = queryParam;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Boolean getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Boolean searchCount) {
		this.searchCount = searchCount;
	}

	public Set<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(Set<String> columnNames) {
		this.columnNames = columnNames;
	}

	public Boolean getEnablePage() {
		return enablePage;
	}

	public void setEnablePage(Boolean enablePage) {
		this.enablePage = enablePage;
	}

	public Boolean getIgnoreCondition() {
		return ignoreCondition;
	}

	public void setIgnoreCondition(Boolean ignoreCondition) {
		this.ignoreCondition = ignoreCondition;
	}

	@Override
	public QueryParamDTO clone() {
		QueryParamDTO queryParamDTO = new QueryParamDTO();
		queryParamDTO.setQueryParam(getQueryParam());
		queryParamDTO.setSortColumn(getSortColumn());
		queryParamDTO.setSortOrder(getSortOrder());
		queryParamDTO.setPageSize(getPageSize());
		queryParamDTO.setCurrentPage(getCurrentPage());
		queryParamDTO.setOffset(getOffset());
		queryParamDTO.setLimit(getLimit());
		queryParamDTO.setSearchCount(getSearchCount());
		queryParamDTO.setColumnNames(getColumnNames());
		queryParamDTO.setEnablePage(getEnablePage());
		queryParamDTO.setIgnoreCondition(getIgnoreCondition());
		return queryParamDTO;
	}
}
