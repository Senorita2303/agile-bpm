package com.dstz.sys.api.vo;

import com.dstz.base.api.model.Tree;

import java.io.Serializable;
import java.util.List;

/**
 * @Name SysDataDictVO
 * @description: Data dictionary
 */
public class SysDataDictVO implements Tree<SysDataDictVO>, Serializable {

	private static final long serialVersionUID = -1706296576459495977L;
	/**
	 * ID
	 */
	private String id;

	/**
	 * Superior id
	 */
	private String parentId;

	/**
	 * Code
	 */
	private String code;

	/**
	 * name
	 */
	private String name;

	/**
	 * Dictionary key
	 */
	private String dictKey;

	/**
	 * Group dictionary code
	 */
	private String typeCode;
	/**
	 * Is the dictionary enabled?
	 */
	private Integer disable;
	/**
	 * Sort
	 */
	private Integer sn;

	/**
	 * dict/node dictionary item
	 */
	private String dictType;

	/**
	 * Extension field 1
	 */
	private String extend1;

	/**
	 * Extension field 2
	 */
	private String extend2;

	/**
	 * Is it built-in in the system?
	 */
	private Integer isSystem;

	private List<SysDataDictVO> children;

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

	public Integer getIsSystem() {
		return isSystem;
	}

	public void setIsSystem(Integer isSystem) {
		this.isSystem = isSystem;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getParentId() {
		return parentId;
	}

	@Override
	public List<SysDataDictVO> getChildren() {
		return children;
	}

	@Override
	public void setChildren(List<SysDataDictVO> list) {
		this.children = list;
	}

	@Override
	public String toString() {
		return "SysDataDictVO{" + "id='" + id + '\'' + ", parentId='" + parentId + '\'' + ", code='" + code + '\''
				+ ", name='" + name + '\'' + ", dictKey='" + dictKey + '\'' + ", typeCode='" + typeCode + '\'' + ", sn="
				+ sn + ", dictType='" + dictType + '\'' + ", extend1='" + extend1 + '\'' + ", extend2='" + extend2
				+ '\'' + ", isSystem=" + isSystem + ", children=" + children + '}';
	}
}
