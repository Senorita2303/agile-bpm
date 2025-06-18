package com.dstz.sys.rest.model.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Name GetDictVO
 * @description: Get dictionary DTO
 */
public class GetDictDTO implements Serializable {
	private static final long serialVersionUID = 3253146726465614563L;

	@NotNull(message = "dictKey cannot be empty!")
	private String dictKey;
	private String rootName;
	private Boolean hasRoot;

	public String getDictKey() {
		return dictKey;
	}

	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public Boolean getHasRoot() {
		if (hasRoot == null) {
			return false;
		}
		return hasRoot;
	}

	public void setHasRoot(Boolean hasRoot) {
		this.hasRoot = hasRoot;
	}

	@Override
	public String toString() {
		return "GetDictDTO{" + "dictKey='" + dictKey + '\'' + ", rootName='" + rootName + '\'' + ", hasRoot=" + hasRoot
				+ '}';
	}
}
