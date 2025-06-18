package com.dstz.springboot.autoconfigure.ldap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ab ldap attributes
 *
 */
@ConfigurationProperties(prefix = "ab.ldap")
public class AbLdapProperties {

	/**
	 * Ldap service address
	 */
	private String server;

	/**
	 * Ldap management domain
	 */
	private String managerDN;

	/**
	 * Ldap administrator password
	 */
	private String managerPassword;

	/**
	 * Ldap authentication method
	 */
	private String authenticationType;

	/**
	 * Set the base search path for the query
	 */
	private String userSearchBase = StringUtils.EMPTY;

	/**
	 * User query filter, {0} is the incoming account name
	 */
	private String userSearchFilter = "uid={0}";

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getManagerDN() {
		return managerDN;
	}

	public void setManagerDN(String managerDN) {
		this.managerDN = managerDN;
	}

	public String getManagerPassword() {
		return managerPassword;
	}

	public void setManagerPassword(String managerPassword) {
		this.managerPassword = managerPassword;
	}

	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public String getUserSearchBase() {
		return userSearchBase;
	}

	public void setUserSearchBase(String userSearchBase) {
		this.userSearchBase = userSearchBase;
	}

	public String getUserSearchFilter() {
		return userSearchFilter;
	}

	public void setUserSearchFilter(String userSearchFilter) {
		this.userSearchFilter = userSearchFilter;
	}
}
