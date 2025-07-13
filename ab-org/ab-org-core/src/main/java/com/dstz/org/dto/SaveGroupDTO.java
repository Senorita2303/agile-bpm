package com.dstz.org.dto;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class SaveGroupDTO implements java.io.Serializable {

	private String id;

	/**
	 * Name
	 */
	@NotBlank(message = "Organization name cannot be empty")
	private String name;

	/**
	 * Parent ID
	 */
	private String parentId;

	/**
	 * Order
	 */

	private Integer sn;

	@NotBlank(message = "Organization code cannot be empty")
	private String code;

	/**
	 * Type: 0 Group, 1 Company, 3 Department
	 */
	private String type;

	/**
	 * describe
	 */
	private String desc;

	/**
	 * Organizational path
	 */
	private String path;

	/**
	 * Positions
	 */
	private List<OrgRelationDTO> orgRelationList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<OrgRelationDTO> getOrgRelationList() {
		return orgRelationList;
	}

	public void setOrgRelationList(List<OrgRelationDTO> orgRelationList) {
		this.orgRelationList = orgRelationList;
	}

}
