package com.dstz.sys.api.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Name SysConnectRecordDTO
 * @description: Public business related record DTO
 */
public class SysConnectRecordDTO implements Serializable {
	private static final long serialVersionUID = 8745568231922260290L;

	/**
	 * Type
	 */
	@NotEmpty(message = "Associated type cannot be empty")
	protected String type;
	/**
	 * Source ID
	 */
	@NotEmpty(message = "The associated source ID cannot be empty")
	protected String sourceId;
	/**
	 * Association ID
	 */
	@NotEmpty(message = "The associated target ID cannot be empty")
	protected String targetId;
	/**
	 * Prompt information
	 */
	@NotEmpty(message = "The associated prompt information cannot be empty")
	protected String notice;

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Return Type
	 *
	 * @return
	 */
	public String getType() {
		return this.type;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * Return source ID
	 *
	 * @return
	 */
	public String getSourceId() {
		return this.sourceId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 * Returns association ID
	 *
	 * @return
	 */
	public String getTargetId() {
		return this.targetId;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	/**
	 * Return to prompt information
	 *
	 * @return
	 */
	public String getNotice() {
		return this.notice;
	}
}
