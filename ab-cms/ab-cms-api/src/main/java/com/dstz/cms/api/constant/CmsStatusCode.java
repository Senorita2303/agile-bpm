package com.dstz.cms.api.constant;

import com.dstz.base.common.codes.IBaseCode;

/**
 * @description CMS module error code enumeration class
 */
public enum CmsStatusCode implements IBaseCode {

	PARAM_INVALID("paramInvalid", "Invalid parameters, no matching data found"),
	BUS_SRC_REL("busSrcRel", "The resource to be deleted is associated and cannot be deleted."),
	BUS_SRC_STS_UPDATE("busSrcStsUpdate",
			"The resource status to be operated has changed and the operation cannot be performed"),
	NOTIFY_READ_ONLY("notifyReadOnly",
			"The current announcement has been published. The published announcement cannot be modified."),
	NEWS_READ_ONLY("newsReadOnly", "The current news has been published. The published news cannot be modified."),
	NOTIFY_DELETE_DISABLED("notifyDeleteDisabled",
			"The current announcement is in the release state, please remove the announcement before deleting it!"),
	NEWS_DELETE_DISABLED("newsDeleteDisabled",
			"The current news is in the release state, please remove the announcement before deleting it!"),
	DOCUMENT_DELETE_FAIL("documentDeleteFail",
			"Deletion failed, there are documents in this directory, please modify the directory to which the documents belong first!"),
	BORROW_FAIL("borrowFail", "Borrowing failed, borrowing has been applied for or approved!"),
	DOWNLOAD_FAIL("downloadFail", "Download failed, missing required parameters!"),
	DOWNLOAD_ERROR("downloadError", "Download error, please contact the administrator!"),
	SAVE_ERROR("saveError", "Failed to save, please try again later"),
	COLUMN_TOO_LONG("columnTooLong", "The field [{}] is too long and exceeds the database field limit!"),
	PORTAL_DELETE_FAIL("portalDeleteFail", "【{}】 is a system built-in portal, deletion is prohibited!");

	private final String code;
	private final String message;

	CmsStatusCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
