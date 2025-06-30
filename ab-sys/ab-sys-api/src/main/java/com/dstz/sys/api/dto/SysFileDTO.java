package com.dstz.sys.api.dto;

import java.io.Serializable;

/**
 * @Name SysFileDTO
 */
public class SysFileDTO implements Serializable {

	private static final long serialVersionUID = 645973812881769440L;
	protected String id;
	/**
	 * Attachment name
	 */
	private String name;

	/**
	 * Attachment size
	 */
	private Long size;
	/**
	 * <pre>
	 * This attachment uses the uploader
	 * For specific types, see the implementation class of IUploader
	 * </pre>
	 */
	private String uploader;
	/**
	 * <pre>
	 * Path, this path can get the corresponding attachment content from the uploader
	 * So it is not necessarily the path, and different uploaders will have different values
	 * </pre>
	 */
	private String path;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public String getPath() {
		return path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
}
