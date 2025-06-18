package com.dstz.sys.rest.model.dto;

import java.io.Serializable;

/**
 * @Name OnlineDocParam
 * @description: Online document parameter DTO
 */
public class OnlineDocParam implements Serializable {
	/**
	 * User id
	 */
	private String userId;
	/**
	 * Username
	 */
	private String userName;
	/**
	 * User avatar
	 */
	private String userAvatar;
	/**
	 * File id
	 */
	private String fileId;
	/**
	 * File name
	 */
	private String fileName;
	/**
	 * File path
	 */
	private String filePath;
	/**
	 * Use permissions
	 */
	private Integer userRight;
	/**
	 * Whether to save automatically, the default is false
	 */
	private Boolean saveFlag;
	/**
	 * callback url
	 */
	private String callbackUrl;
	/**
	 * Additional parameters
	 */
	private Object extraData;
	/**
	 * Button permissions
	 */
	private String userMenuPermission;

	public OnlineDocParam(String fileId) {
		this.fileId = fileId;
	}

	public OnlineDocParam() {
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

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
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

	public void setUserMenuPermission(String userMenuPermission) {
		this.userMenuPermission = userMenuPermission;
	}

	@Override
	public String toString() {
		return "OnlineDocParam{" + "userId='" + userId + '\'' + ", userName='" + userName + '\'' + ", userAvatar='"
				+ userAvatar + '\'' + ", fileId='" + fileId + '\'' + ", fileName='" + fileName + '\'' + ", filePath='"
				+ filePath + '\'' + ", userRight=" + userRight + ", saveFlag=" + saveFlag + ", callbackUrl='"
				+ callbackUrl + '\'' + ", extraData=" + extraData + ", userMenuPermission='" + userMenuPermission + '\''
				+ '}';
	}
}
