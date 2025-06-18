package com.dstz.sys.rest.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Name SysPropertiesVo
 */
public class SysPropertiesVO implements Serializable {

	private static final long serialVersionUID = -6671077622293070668L;
	private String id;

	private String code;

	private String name;

	private String value;

	private String desc;

	private String encrypt;

	private String environment;

	private String group;

	/**
	 * 分类使用逗号进行分割。
	 */
	protected List<String> categorys = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<String> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<String> categorys) {
		this.categorys = categorys;
	}
}
