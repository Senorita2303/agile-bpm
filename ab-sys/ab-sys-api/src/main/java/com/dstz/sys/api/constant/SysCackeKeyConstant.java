package com.dstz.sys.api.constant;
/**
 * System cache key definition
 */
public class SysCackeKeyConstant {


    /**
     * Dictionary method cache key
     */
    public static final String GET_DICT_NODE_LIST = "getDictNodeListEl:";

    /**
     * Dictionary method cache key SpEL expression
     */
    public static final String GET_DICT_NODE_LIST_EL = "'getDictNodeListEl:'";

    /**
     * Dictionary method cache key SpEL expression
     */
    public static final String GET_DICT_NODE_LIST_RECEIVE_EL = GET_DICT_NODE_LIST_EL + ".concat(#root.args[0])";


    /**
     * Message template method cache key SpEL expression
     */
    public static final String GET_MESSAGE_TEMPLATE_LIST = "'getMessageTemplateListEl:'";

    /**
     * Dictionary method cache key SpEL expression
     */
    public static final String ET_MESSAGE_TEMPLATE_LIST_RECEIVE_EL = GET_MESSAGE_TEMPLATE_LIST + ".concat(#root.args[0])";

    /**
     * System configuration cache key
     */
    public static final String PROPERTIES_CACHE_REGION_RECEIVE_KEY = "getPropertiesCacheEl:" ;


    public static final String PROPERTIES_CACHE_REGION_RECEIVE_KEY_EL = "'getPropertiesCacheEl:'" ;

    /**
     * System configuration cache key SpEL expression
     */
    public static final String PROPERTIES_CACHE_REGION_RECEIVE_EL = PROPERTIES_CACHE_REGION_RECEIVE_KEY_EL + ".concat(#root.args[0])";

    public static final String INF_WHITELIST_KEY = "infWhiteList";

    public static final String INF_INGORES_KEY = "infIngores";

    public static final String INF_CSRF_INGORES_KEY = "infCsrfIngores";


    /**
     * Cache area-Logged in user company
     */
    public static final String REGION_LOGIN_COMPANY = "LOGIN_COMPANY";


    /**
     * User work reception-EL expression
     *
     * <p>userWorkReceive:{userId}</p>
     */
    public static final String USER_WORK_RECEIVE_EL = "'userWorkReceive:'.concat(#root.args[0])";

    /**
     * User work receiving-string formatting
     *
     * <p>userWorkReceive:{userId}</p>
     */
    public static final String USER_WORK_RECEIVE_FORMAT = "userWorkReceive:%s";
}
