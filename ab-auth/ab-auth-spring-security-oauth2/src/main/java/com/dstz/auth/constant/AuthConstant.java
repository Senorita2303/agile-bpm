package com.dstz.auth.constant;

/**
 * Authorization verification constants
 *
 */
public interface AuthConstant {

	/**
	 * Table fields
	 */
	String CLIENT_FIELDS = "client_id_, CONCAT('{noop}',client_secret_) as client_secret_, resource_ids_, scope_, grant_types_, " +
		"redirect_uri_, authorities_, access_token_validity_, " +
		"refresh_token_validity_, commentate_, autoapprove_, status_, is_deleted_, create_time_, create_by_, create_org_id_, update_time_,updater_,update_by_";

	/**
	 * Query Statement
	 */
	String BASE_STATEMENT = "select " + CLIENT_FIELDS + " from oauth_client";

	/**
	 * Query sorting
	 */
	String DEFAULT_FIND_STATEMENT = BASE_STATEMENT + " order by client_id_";

	/**
	 * Query conditions
	 */
	String DEFAULT_SELECT_STATEMENT = BASE_STATEMENT + " where client_id_ = ?";

    /**
     * oauth built-in interface collection
     */
	String [] OAUTH_DEFAULT_URL = new String[]{"/oauth/token","/oauth/authorize"};

    /**
     * oauth grant_type
     */
    String GRANT_TYPE = "grant_type";

    /**
     * grant_type authorization_code
     */
    String AUTHORIZATION_CODE = "authorization_code";

}
