package com.dstz.base.common.constats;

/**
 * Cache area constants
 *
 */
public class AbCacheRegionConstant {

	private AbCacheRegionConstant() throws IllegalAccessException {
		throw new IllegalAccessException();
	}

	/**
	 * Login related token
	 */
	public static final String LOGIN_CACHE_REGION = "LOGIN_TOKEN";

	/**
	 * User password recovery
	 */
	public static final String LOGIN_PWD_REGION = "PWD_TOKEN";

	/**
	 * System resource related
	 */
	public static final String SYS_RESOURCE = "SYS_RESOURCE";

	/**
	 * System resources all resource urls
	 */
	public static final String SYS_RESOURCE_ALLURL = "SYS_RESOURCE_ALLURL";
	/**
	 * Work handover related
	 */
	public static final String WORK_HANDOVER_REGION = "WORK_HANDOVER";

	/**
	 * System properties want to turn off
	 */
	public static final String PROPERTIES_CACHE_REGION = "SYSPROPETIES";

	/**
	 * Dictionary related
	 */
	public static final String DICT_CACHE_REGION = "DICT";

	/**
	 * Audit log meta information
	 */
	public static final String AUDIT_LOG_META = "AUDIT_LOG_META";

	/**
	 * News related
	 */
	public static final String MSG_REGION = "MSG_REGION";

	/**
	 * Third-party login token
	 */
	public static final String THIRD_TOKEN = "THIRD_TOKEN";

	/**
	 * agileBPM Process Definition
	 */
	public static final String BPM_PROCESS_DEF = "BPM_PROCESS_DEF";

	/**
	 * activiti ProcessDefinitionEntity
	 */
	public static final String BPM_ACT_PROCESS_DEF = "BPM_ACT_PROCESS_DEF";

	/**
	 * Data permissions
	 */
	public static final String DATA_PRIVILEGE = "DATA_PRIVILEGE";

	/**
	 * Caching of business objects
	 */
	public static final String BIZ_OBJECT_REGION = "BIZ_OBJECT";

	/**
	 * System Application
	 */
	public static final String SYS_APPLICATION = "SYS_APPLICATION";

	/**
	 * System Session Properties
	 */
	public static final String SYS_SESSION_ATTRIBUTE = "SYS_SESSION_ATTRIBUTE";

	/**
	 *
	 */
	public static final String OAUTH_REGION = "";

	public static final String SALARY_CALCULATE = "SALARY_CALCULATE";

	/**
	 * User registration related
	 */
	public static final String USER_REGISTER_REGION = "USER_REGISTER";

	public static final String APPSTORE_TRAIL_INFO = "APPSTORE_TRAIL_INFO";

	/**
	 * SMS verification code
	 */
	public static final String VERIFICATION_CODE = "VERIFICATION_CODE";
}
