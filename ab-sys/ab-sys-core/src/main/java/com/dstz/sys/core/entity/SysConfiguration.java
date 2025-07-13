package com.dstz.sys.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dstz.base.entity.AbModel;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.io.Serializable;

@TableName("sys_configuration")
public class SysConfiguration extends AbModel<SysConfiguration> {

	@TableId(value = "id_", type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * Configuration type
	 */
	@TableField("code_")
	private String code;

	/**
	 * Configure the environment
	 */
	@TableField("env_")
	private String env;

	/**
	 * Configuration json
	 */
	@TableField("json_")
	private String json;

	/**
	 * Is it available?
	 */
	@TableField("is_enable_")
	private Integer isEnable;

	/**
	 * Is it encrypted?
	 */
	@TableField("is_encrypt_")
	private Integer isEncrypt;

	/**
	 * Is it encrypted?
	 */
	@TableField("name_")
	private String name;

	/**
	 * Is it encrypted?
	 */
	@TableField("desc_")
	private String desc;

	/**
	 * Creator
	 */
	@TableField(value = "create_by_", fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * Creation time
	 */
	@TableField(value = "create_time_", fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * Modifier
	 */
	@TableField(value = "update_by_", fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * Modification time
	 */
	@TableField(value = "update_time_", fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getIsEncrypt() {
		return isEncrypt;
	}

	public void setIsEncrypt(Integer isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String getCreateBy() {
		return createBy;
	}

	@Override
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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
	public String getUpdateBy() {
		return updateBy;
	}

	@Override
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Override
	public Date getUpdateTime() {
		return updateTime;
	}

	@Override
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public SysConfiguration() {
	}

	public SysConfiguration(String id, String code, String env, String json, Integer isEnable, Integer isEncrypt,
			String name, String desc, String createBy, Date createTime, String updateBy, Date updateTime) {
		this.id = id;
		this.code = code;
		this.env = env;
		this.json = json;
		this.isEnable = isEnable;
		this.isEncrypt = isEncrypt;
		this.name = name;
		this.desc = desc;
		this.createBy = createBy;
		this.createTime = createTime;
		this.updateBy = updateBy;
		this.updateTime = updateTime;
	}
}
