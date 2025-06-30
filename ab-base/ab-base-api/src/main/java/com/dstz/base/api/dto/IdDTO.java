package com.dstz.base.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * ID Data Transfer Object
 *
 */
public class IdDTO implements java.io.Serializable {

	private static final long serialVersionUID = -1631823931282821679L;

	/**
	 * id
	 */
	@NotEmpty(message = "id cannot be empty")
	@Size(max = 64, message = "id is too long")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "IdDTO{" + "id='" + id + '\'' + '}';
	}
}
