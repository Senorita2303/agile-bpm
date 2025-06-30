package com.dstz.sys.rest.model.dto;

import com.dstz.sys.api.constant.RightsObjectConstants;
import com.dstz.sys.core.entity.SysAuthorization;

import java.io.Serializable;
import java.util.List;

/**
 * @Name SaveAuthorization
 */
public class AuthorizationDTO implements Serializable {

	private static final long serialVersionUID = 2808337286344024676L;

	private String rightsTarget;
	private RightsObjectConstants rightsObject;
	private List<SysAuthorization> authorizationList;

	public String getRightsTarget() {
		return rightsTarget;
	}

	public void setRightsTarget(String rightsTarget) {
		this.rightsTarget = rightsTarget;
	}

	public RightsObjectConstants getRightsObject() {
		return rightsObject;
	}

	public void setRightsObject(RightsObjectConstants rightsObject) {
		this.rightsObject = rightsObject;
	}

	public List<SysAuthorization> getAuthorizationList() {
		return authorizationList;
	}

	public void setAuthorizationList(List<SysAuthorization> authorizationList) {
		this.authorizationList = authorizationList;
	}

	public AuthorizationDTO() {
	}

	public AuthorizationDTO(String rightsTarget, RightsObjectConstants rightsObject,
			List<SysAuthorization> authorizationList) {
		this.rightsTarget = rightsTarget;
		this.rightsObject = rightsObject;
		this.authorizationList = authorizationList;
	}
}
