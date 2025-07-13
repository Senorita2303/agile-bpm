package com.dstz.base.common.script.variables;

import java.util.List;
import java.util.stream.Collectors;

import com.dstz.org.api.model.IGroup;

public class RoleListVariable {
	
	private List<IGroup> roleList ;
	
	public RoleListVariable(List<IGroup> roleList2) {
		this.roleList = roleList2 ;
	}

	public List<String> getIdList() {
		List<IGroup> roleList = getRoleList();
		return roleList.stream().map(IGroup::getGroupId).collect(Collectors.toList());
	}
	 
	public List<String> getCodeList() {
		List<IGroup> roleList = getRoleList();
		return roleList.stream().map(IGroup::getGroupCode).collect(Collectors.toList());
	}
	
	/**
	 *  Get the maximum level
	 * @return
	 */
	public int getMaxLevel() {
		List<IGroup> roleList = getRoleList();
		int level = 0;
		for(IGroup group : roleList) {
			Integer groupLevel = group.getAttrValue("level", Integer.class);
			if(groupLevel != null && groupLevel > level ) {
				level = groupLevel;
			}
		}
		return level;
	}
	
	public List<IGroup> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<IGroup> roleList) {
		this.roleList = roleList;
	}
}
