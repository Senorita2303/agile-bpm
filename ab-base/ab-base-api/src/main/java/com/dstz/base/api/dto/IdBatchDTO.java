package com.dstz.base.api.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * Bulk Data Transfer Object
 *
 */
public class IdBatchDTO implements java.io.Serializable {

	private static final long serialVersionUID = 2403594070347388576L;

	/**
	 * Batch ID
	 */
	@NotEmpty(message = "Batch ID cannot be empty")
	private List<Serializable> ids;

	public List<Serializable> getIds() {
		return ids;
	}

	public void setIds(List<Serializable> ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		return "IdBatchDTO{" + "ids=" + ids + '}';
	}
}
