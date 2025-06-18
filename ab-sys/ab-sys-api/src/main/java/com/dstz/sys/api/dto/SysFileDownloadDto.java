package com.dstz.sys.api.dto;

import javax.validation.constraints.NotEmpty;

public class SysFileDownloadDto {
	/**
	 * File id
	 */
	@NotEmpty(message = "The file id cannot be empty")
	private String id;
	/**
	 *The key behind the file id encrypted by token
	 */
	@NotEmpty(message = "The download key cannot be empty")
	private String key;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
