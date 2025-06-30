package com.dstz.sys.rest.model.vo;

import java.io.Serializable;

/**
 * @Name createAndOpenVO
 * @description: Create a document and open the document result VO
 */
public class CreateAndOpenVO implements Serializable {

	private String url;
	private String fileId;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public CreateAndOpenVO(String url, String fileId) {
		this.url = url;
		this.fileId = fileId;
	}

	public CreateAndOpenVO() {
	}

	@Override
	public String toString() {
		return "createAndOpenVO{" + "url='" + url + '\'' + ", fileId='" + fileId + '\'' + '}';
	}
}
