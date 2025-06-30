package com.dstz.sys.rest.model.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Name OnlineDocJsonParamDTO
 * @description: Online document request interface DTO
 */
public class OperateOnlineDocDTO implements Serializable {
	/**
	 * File id
	 */
	@NotNull(message = "File Id cannot be empty")
	private String fileId;

	/**
	 * File name, required when creating a file
	 */
	private String fileName;

	/**
	 * Use permissions
	 */
	private Integer userRight;
	/**
	 * Whether to save automatically, the default is false
	 */
	private Boolean saveFlag;

	/**
	 * Additional parameters
	 */
	private Object extraData;
	/**
	 * Button permissions
	 */
	private String userMenuPermission;

	public OperateOnlineDocDTO(String fileId) {
		this.fileId = fileId;
	}

	public OperateOnlineDocDTO(String fileId, Integer userRight) {
		this.fileId = fileId;
		this.userRight = userRight;
	}

	public OperateOnlineDocDTO() {
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public Integer getUserRight() {
		return userRight;
	}

	public void setUserRight(Integer userRight) {
		this.userRight = userRight;
	}

	public Boolean getSaveFlag() {
		if (saveFlag == null) {
			return Boolean.FALSE;
		}
		return saveFlag;
	}

	public void setSaveFlag(Boolean saveFlag) {
		this.saveFlag = saveFlag;
	}

	public Object getExtraData() {
		return extraData;
	}

	public void setExtraData(Object extraData) {
		this.extraData = extraData;
	}

	public String getUserMenuPermission() {
		return userMenuPermission;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setUserMenuPermission(String userMenuPermission) {
		this.userMenuPermission = userMenuPermission;
	}

	@Override
	public String toString() {
		return "OperateOnlineDocDTO{" + "fileId='" + fileId + '\'' + ", fileName='" + fileName + '\'' + ", userRight="
				+ userRight + ", saveFlag=" + saveFlag + ", extraData=" + extraData + ", userMenuPermission='"
				+ userMenuPermission + '\'' + '}';
	}
}
