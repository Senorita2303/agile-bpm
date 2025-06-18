package com.dstz.auth.authentication.vo;

/**
 * System Application
 *
 */
public class SysApplicationVO implements java.io.Serializable {

	private static final long serialVersionUID = 5700878947689459575L;

	/**
	 * Code
	 */
	private String code;

	/**
	 * Key
	 */
	private String secret;

	/**
	 * Scope of Authorization
	 */
	private String scope;

	/**
	 * Resource Collection
	 */
	private String resourceIds;

	/**
	 * Authorization Type
	 */
	private String grantTypes;

	/**
	 * Callback address
	 */
	private String redirectUri;

	/**
	 * Automatic authorization
	 */
	private Integer autoapprove;

	/**
	 * Validity
	 */
	private Integer accessTokenValidity;

	/**
	 * Refresh seconds
	 */
	private Integer refreshTokenValidity;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(String resourceIds) {
		this.resourceIds = resourceIds;
	}

	public String getGrantTypes() {
		return grantTypes;
	}

	public void setGrantTypes(String grantTypes) {
		this.grantTypes = grantTypes;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public Integer getAutoapprove() {
		return autoapprove;
	}

	public void setAutoapprove(Integer autoapprove) {
		this.autoapprove = autoapprove;
	}

	public Integer getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public void setAccessTokenValidity(Integer accessTokenValidity) {
		this.accessTokenValidity = accessTokenValidity;
	}

	public Integer getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

	public void setRefreshTokenValidity(Integer refreshTokenValidity) {
		this.refreshTokenValidity = refreshTokenValidity;
	}
}
