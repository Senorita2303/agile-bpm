package com.dstz.auth.authentication.api.constant;

public class AuthApiConstant {
	private AuthApiConstant() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	/**
	 * Source - system
	 */
	public static final String SYSTEM = "system";
	/**
	 * System alias
	 */
	public static final String AGILEBPM = "agilebpm";
	/**
	 * Login timeout jump url
	 */
	public static final String SSO_REDIRECTURL = "SSO_redirectUrl";

	/**
	 * Super-managed accounts
	 */
	public static final String ADMIN = "admin";

	//----------oauth2 related configuration------------

	/**
	 * The default validity period of the token is 2 hours
	 */
	public static final Integer ACCESSTOKEN_VALIDITY_SECONDS = 7200;

	/**
	 * The refresh token is valid for 3 days by default.
	 */
	public static final Integer REFRESHTOKEN_VALIDITY_SECONDS = 259200;

	public static final String AUTHORIZATION = "Authorization";

	public static final String BEARER = "Bearer";

	/**
	 * Configure token endpoint security constraints
	 */
	public static final String TOKEN_SERVER_SECURITY_CONFIGURER = "permitAll()";

	/**
	 * Customized authorization page
	 */
	public static final String TOKEN_SERVER_AUTH_DEFAULTPATH = "/oauth/confirm_access";

	/**
	 * Customized authorization page
	 */
	public static final String TOKEN_SERVER_AUTH_CUSTOMPATH = "/customer/confirm_access";

	/**
	 * oauth client key
	 */
	public static final String OAUTH_TOKEN_CLIENT_KEY = "client_id";

	public static final String OAUTH_TOKEN_CLIENT_SECRET = "client_secret";

	/**
	 * oauth client refresh_token
	 */
	public static final String OAUTH_TOKEN_REFRESH_TOKEN = "refresh_token";

	/**
	 * oauth client grant_type
	 */
	public static final String OAUTH_TOKEN_GRANT_TYPE = "grant_type";

	/**
	 * oauth client username
	 */
	public static final String OAUTH_TOKEN_USER_NAME = "username";

	/**
	 * oauth client password
	 */
	public static final String OAUTH_TOKEN_PASSWORD = "password";

}
