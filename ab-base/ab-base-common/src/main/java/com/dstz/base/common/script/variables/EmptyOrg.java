package com.dstz.base.common.script.variables;

import com.dstz.org.api.model.IGroup;

public class EmptyOrg implements IGroup{

	@Override
	public String getGroupId() {
		return "";
	}

	public String getId() {
		return "";
	}

	@Override
	public String getGroupName() {
		return "";
	}

	@Override
	public String getGroupType() {
		return "";
	}

	@Override
	public String getGroupCode() {
		return "";
	}

	@Override
	public <T> T getAttrValue(String attrName, Class<T> tClass) {
		return null;
	}

	@Override
	public Integer getGroupLevel() {
		return 0;
	}
}
