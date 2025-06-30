package com.dstz.base.api.vo;

import java.util.Collections;
import java.util.List;

import com.dstz.base.api.dto.PageListDTO;

/**
 * Since pageListDTO is implemented as a list, there are some problems with its deserialization
 *
 * @param <T>
 */
public class PageListVO<T> {
	/**
	 * Page size
	 */
	private long pageSize = 0;
	/**
	 * Current Page
	 */
	private long page = 1;
	/**
	 * Total number of items
	 */
	private long total = 0L;
	/**
	 * Paginated list data
	 */
	private List<T> rows = null;

	public PageListVO() {
	}

	public PageListVO(PageListDTO listDTO) {
		super();
		this.pageSize = listDTO.getPageSize();
		this.page = listDTO.getPage();
		this.total = listDTO.getTotal();
		this.rows = listDTO.getRows();
		if (rows == null) {
			this.rows = Collections.emptyList();
		}
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
