package com.dstz.base.common.script.variables;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.utils.CastUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.org.api.GroupApi;
import com.dstz.org.api.UserApi;
import com.dstz.org.api.enums.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;

import java.util.List;

/**
 * Lazy loading of user information through objects in groovy
 * For example, the script: return ( startUser.orgType.equals( "1" ) && startUser.username.equals( "" ) )
 *
 */
public class UserGroovyVariable {
	private IUser user;
	private IGroup org;
	private RoleListVariable role;
	
	// Load by id when not set
	private String orgId;
	private String userId;
	
	
	/**
	 * There are users and organization information that needs to be lazy loaded
	 * @param user
	 * @param orgId
	 */
	public UserGroovyVariable(IUser user, String orgId) {
		this.user = user;
		this.orgId = orgId;
	}
	/**
	 * Only users, organization takes the current organization
	 * @param user2
	 */
	public UserGroovyVariable(IUser user2) {
		this.user = user2;
 	}
	
	/**
	 * When both users and organizations have only IDs
	 * @param userId
	 * @param orgId
	 */
	public UserGroovyVariable(String userId, String orgId) {
		this.userId = userId;
		this.orgId = orgId;
	}

	public String getId() {
		return getUser().getUserId();
	}
	

	private IUser getUser() {
		if(user != null) return user;
		
		if(StrUtil.isNotBlank(userId)) {
			UserApi userApi = SpringUtil.getBean(UserApi.class);
			Assert.notNull(userApi, "Please check your organization service injection!");
			user = userApi.getByUserId(userId);
		}
		
		return user;
	}

	public String getUsername() {
		return getUser().getUsername();
	}

	public IGroup getOrg() {
		if(org != null)return org;
		
		// Specify ORG
		if( StrUtil.isNotBlank(orgId)) {
			GroupApi groupApi = SpringUtil.getBean(GroupApi.class);
			Assert.notNull(groupApi, "Please check your organization service injection!");
			
			org = groupApi.getByGroupId(GroupType.ORG.getType(), orgId);
			return org;
		} 
		
		// Get the current organization
		org = UserContextUtils.getGroup().orElse(null);
		if(org == null) {
			org =  new EmptyOrg();
		}
		return org;
	}

	public RoleListVariable getRole() {
		if(role == null) {
			GroupApi groupApi = SpringUtil.getBean(GroupApi.class);
			Assert.notNull(groupApi, "Please check your organization service injection!");
			List<? extends IGroup> roleList = groupApi.getByGroupTypeAndUserId(GroupType.ROLE.getType(), getUser().getUserId());
			this.role = new RoleListVariable(CastUtils.cast(roleList));
		}
		
		return role;
	}
	
	
	
	 /**
     * Get User ID
     *
     * @return User ID
     */
    String getUserId() {
		return getUser().getUserId();
	}
    /**
     * Get attribute value
     *
     * @param attrName Attribute Name
     * @param tClass   Attribute Type
     * @param <T>      T
     * @return Attribute Value
     */
    <T> T getAttrValue(String attrName, Class<T> tClass) {
		return getUser().getAttrValue(attrName, tClass);
	}
    
    
	public String getOrgId() {
		return getOrg().getGroupId();
	}
	
 
	public String getOrgName() {
		return getOrg().getGroupName();
	}

	public Integer getOrgType() {
		return getOrg().getAttrValue("type", Integer.class);
	}

	public String getOrgCode() {
		return getOrg().getGroupCode();
	}
}
