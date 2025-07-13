package com.dstz.springboot.autoconfigure.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Authentication Configuration
 */
@ConfigurationProperties(prefix = "ab.security")
public class AbSecurityProperties {

	/**
	 * Comma separated
	*/

	/**Ignore xss addresses **/
	private String xssIngores = "";
	/**Ignore cross-domain access addresses **/
	private String[] csrfIngores = new String[] { "127.0.0.1" };
	/**Ignore authentication address **/
	private String[] authIngores = new String[] { "/login.*" };

	/**
	 * Maximum number of sessions
	 * <ul>
	 *     <li>-1: Indicates that the session is not subject to any restrictions</li>
	 *     <li>0: Limit the last login session of the logged-in user, and all other sessions will be invalid</li>
	 *     <li>&gt;0: Limit the maximum number of sessions for a user</li>
	 * </ul>
	 */
	private Integer maximumSessions = -1;

	/**
	 * Enable cross-domain interception
	 */
	private Boolean enableCors = Boolean.TRUE;

	/**
	 * Enable cross-site blocking
	 */
	private Boolean enableCsrf = Boolean.TRUE;

	public String getXssIngores() {
		return xssIngores;
	}

	public void setXssIngores(String xssIngores) {
		this.xssIngores = xssIngores;
	}

	public String[] getCsrfIngores() {
		return csrfIngores;
	}

	public void setCsrfIngores(String[] csrfIngores) {
		this.csrfIngores = csrfIngores;
	}

	public String[] getAuthIngores() {
		return authIngores;
	}

	public void setAuthIngores(String[] authIngores) {
		this.authIngores = authIngores;
	}

	public Integer getMaximumSessions() {
		return maximumSessions;
	}

	public void setMaximumSessions(Integer maximumSessions) {
		this.maximumSessions = maximumSessions;
	}

	public Boolean getEnableCors() {
		return enableCors;
	}

	public void setEnableCors(Boolean enableCors) {
		this.enableCors = enableCors;
	}

	public Boolean getEnableCsrf() {
		return enableCsrf;
	}

	public void setEnableCsrf(Boolean enableCsrf) {
		this.enableCsrf = enableCsrf;
	}
}
