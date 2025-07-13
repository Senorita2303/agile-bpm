package com.dstz.sys.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Data dictionary
 * </p>
 *
 */
@TableName("sys_data_dict")
public class SysDataDict extends AbModel<SysDataDict> {

	public static final String TYPE_DICT = "dict";
	public static final String TYPE_NODE = "node";

	/**
	 * Data dictionary left tree classification code
	 */
	public static final String TYPE_CODE = "system";
	/**
	 * System built-in classification code
	 */
	public static final String SYSTEM_DEFAULT_TYPE = "systemDefault";

	/**
	 * ID
	 */
	@TableId(value = "id_", type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * Superior id
	 */
	@TableField("parent_id_")
	private String parentId = "0";

	/**
	 * Code
	 */
	@TableField("code_")
	private String code;

	/**
	 * name
	 */
	@TableField("name_")
	private String name;

	/**
	 * Dictionary key
	 */
	@TableField("dict_key_")
	private String dictKey;

	/**
	 * Group dictionary code
	 */
	@TableField("type_code_")
	private String typeCode;

	/**
	 * Is it disabled (0: normal 1: disabled)
	 */
	@TableField("disable_")
	private Integer disable = 0;

	/**
	 * Sort
	 */
	@TableField("sn_")
	private Integer sn;

	/**
	 * dict/node dictionary item
	 */
	@TableField("dict_type_")
	private String dictType;

	/**
	 * Extension field 1
	 */
	@TableField("extend1")
	private String extend1;

	/**
	 * Extension field 2
	 */
	@TableField("extend2")
	private String extend2;

	/**
	 * Is it built-in in the system?
	 */
	@TableField("is_system_")
	private Integer isSystem = 0;

	/**
	 * Creation time
	 */
	@TableField(value = "create_time_", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * Creator ID
	 */
	@TableField(value = "create_by_", fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * Organizations affiliated with
	 */
	@TableField(value = "create_org_id_", fill = FieldFill.INSERT)
	private String createOrgId;

	/**
	 * Update time
	 */
	@TableField(value = "update_time_", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	/**
	 * Updater
	 */
	@TableField(value = "updater_", fill = FieldFill.INSERT_UPDATE)
	private String updater;

	/**
	 * Updater ID
	 */
	@TableField(value = "update_by_", fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDictKey() {
		return dictKey;
	}

	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public Integer getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
	}

	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}

	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String getCreateBy() {
		return createBy;
	}

	@Override
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	@Override
	public Date getUpdateTime() {
		return updateTime;
	}

	@Override
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String getUpdater() {
		return updater;
	}

	@Override
	public void setUpdater(String updater) {
		this.updater = updater;
	}

	@Override
	public String getUpdateBy() {
		return updateBy;
	}

	@Override
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public Serializable pkVal() {
		return this.id;
	}
}
