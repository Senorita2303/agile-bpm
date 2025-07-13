package com.dstz.sys.core.entity;

import com.dstz.base.entity.IPersistentEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * <p>
 * Public business related records
 * </p>
 *
 */
@TableName("sys_connect_record")
public class SysConnectRecord extends Model<SysConnectRecord> implements IPersistentEntity {

	@TableId(value = "id_", type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * Type
	 */
	@TableField("type_")
	private String type;

	/**
	 * Source ID
	 */
	@TableField("source_id_")
	private String sourceId;

	/**
	 * Association ID
	 */
	@TableField("target_id_")
	private String targetId;

	/**
	 * Prompt information
	 */
	@TableField("notice")
	private String notice;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

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

	@Override
	public Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysConnectRecord{" + "id=" + id + ", type=" + type + ", sourceId=" + sourceId + ", targetId=" + targetId
				+ ", notice=" + notice + "}";
	}
}
