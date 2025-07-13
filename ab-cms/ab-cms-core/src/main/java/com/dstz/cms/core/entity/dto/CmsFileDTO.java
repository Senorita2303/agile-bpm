package com.dstz.cms.core.entity.dto;

import java.io.Serializable;

/**
 * CMD attachment property DTO object
 */
public class CmsFileDTO implements Serializable {

	private String fileName;

	private String fileSize;

	private String fileType;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public CmsFileDTO(String fileName, String fileSize, String fileType) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.fileType = fileType;
	}

	public CmsFileDTO(String fileName, String fileType) {
		this.fileName = fileName;
		this.fileType = fileType;
	}

	@Override
	public String toString() {
		return "CmsFileDTO{" + "fileName='" + fileName + '\'' + ", fileSize='" + fileSize + '\'' + ", fileType='"
				+ fileType + '\'' + '}';
	}
}
