package com.dstz.sys.api.vo;

import java.io.Serializable;

/**
 * @Name SysFileInfoDTO
 * @description: Attachment upload file information analysis 
 */
public class SysFileVO implements Serializable {
	private static final long serialVersionUID = -4367971857124877587L;

	protected String id;
	/**
	 * Attachment name
	 */
	private String name;

	/**
	 * Attachment size
	 */
	private Long size;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "SysFileVO{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", size=" + size + '}';
	}
}
