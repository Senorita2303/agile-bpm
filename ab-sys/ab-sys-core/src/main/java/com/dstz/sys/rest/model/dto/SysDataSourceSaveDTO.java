package com.dstz.sys.rest.model.dto;

import com.dstz.sys.api.dto.SysDataSourceDefAttribute;

import java.io.Serializable;
import java.util.List;

/**
 * @Name SysDataSourceDefSaveDTO
 * @description: Data source saves DTO
 */
public class SysDataSourceSaveDTO implements Serializable {

	private static final long serialVersionUID = 8463948486020451834L;

	private String id;

	private String alias;

	private String name;

	private String desc;

	private String dbType;

	private String classPath;

	private String defId;

	private List<SysDataSourceDefAttribute> attributeList;

	/**
	 * Configure from library
	 */

	private String replica;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
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

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public List<SysDataSourceDefAttribute> getAttributeList() {
		return attributeList;
	}

	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	public void setAttributeList(List<SysDataSourceDefAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public String getReplica() {
		return replica;
	}

	public void setReplica(String replica) {
		this.replica = replica;
	}
}
