package com.dstz.auth.authentication.api.constant;

import javax.swing.*;

/**
 * Cache key constants
 *
 */
public class AuthCacheKeyConstant {

    /**
     * auth_client method cache key
     */
    public static final String CACHE_REGION_AUTH_CLIENT = "OAUTH_REGION";
    /**
     * auth_client method cache key SpEL expression
     */
    public static final String AUTH_CLIENT_RECEIVE_EL = "'authClientEl:'.concat(#root.args[0])";

    public static final String AUTH_APP_LIST_EL = "'authAppListEl:";

    /**
     * auth_client method cache key SpEL expression
     */
    public static final String CACHE_EVICT_AUTH_CLIENT_RECEIVE_EL = "'authClientEl:'.concat(#root.args[0].getCode())";

    /**
     * Cache permission mapping based on URL
     */
    public static final String URL_ROLE_MAPPING = "agilebpm:sys:resoucesUrlRoleMapping:";
    /**
     * Login user cache key
     */
    public static final String LOGIN_USER_CACHE_KEY = "agilebpm:loginUser:";

    /**
     * User token cache
     */
    public static final String USER_TOKEN = "user:token:";


    /**
     * User login statistics
     */
    public static final String USER_LOGIN_COUNT = "user:login:count:";

    /**
     * Ignore Authentication List
     */
    public static final String IGNORE_AUTH_URL_LIST = "'ignore:auth:url'";

    /**
     * Determine whether the URL requires method authentication
     */
    public static final String IS_ROLE = "'isRole:'.concat(#root.args[0])";


}
