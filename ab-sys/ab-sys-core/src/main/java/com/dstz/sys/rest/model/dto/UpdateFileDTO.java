package com.dstz.sys.rest.model.dto;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @Name UpdateFileDTO
 * @description: Update file dto
 */
public class UpdateFileDTO implements Serializable {
	private String fileId;
	private String userId;
	private String userName;
	private InputStream fileStream;

	public UpdateFileDTO(String fileId, InputStream fileStream) {
		this.fileId = fileId;
		this.fileStream = fileStream;
	}

	public UpdateFileDTO() {
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public InputStream getFileStream() {
		return fileStream;
	}

	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}

	@Override
	public String toString() {
		return "UpdateFileDTO{" + "fileId='" + fileId + '\'' + ", userId='" + userId + '\'' + ", userName='" + userName
				+ '\'' + ", file=" + fileStream + '}';
	}
}
