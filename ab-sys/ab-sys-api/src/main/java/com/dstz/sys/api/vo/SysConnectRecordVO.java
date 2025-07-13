package com.dstz.sys.api.vo;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Name SysConnectRecordVO
 */
public class SysConnectRecordVO implements Serializable {
	private static final long serialVersionUID = 4047121912687250449L;

	protected String type;

	protected String sourceId;

	protected String targetId;

	protected String notice;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
}
