package com.dstz.base.common.property;

public enum PropertyEnum implements IBaseProperty {


    ADMIN_ACCOUNTS("admin.accounts", "admin", "admin account"),
    JDBC_TYPE("spring.jdbc.dbType", "", "Database Type"),
    FORM_DEF_BACKUP_PATH("formDefBackupPath", "", "Form backup address"),
    FORM_TEMPLATE_URL("formTemplateUrl", "src/main/resources/templates", "Form template URL"),

    // Process related

    TASK_REMOVE_MESSAGE_PUSH("task_remove_message_push", false, "Task deletion message push"),
    DO_TASK_SUCCESSIONAL("doTaskSuccessional", false, "When the node behind the task is still its own task, it is processed continuously"),
    BPM_NOT_DEFAULT_BUTTONS("bpmNotDefaultButtons", "oppose,reject2Start,custMultiExecution,addSign,manualEnd", "Process non-default button"),

    // Upload related
    UPLOADER_DEFAULT("uploader.default", "", "Default file uploader"),
    MINIO_BUCKET_NAME("ab.minio.bucketName", "", "minio bucketName"),
    UPLOADER_ORDINARY_PATH("uploader.ordinary.path", "", ""),
    BATCH_INSERT_EXCL_DATA_NUM("batch_insert_excl_data_num", 80, "The number of rows to be inserted in a single batch when importing excl data"),
    EXPORT_EXCL_MAX_NUM("export_excl_max_num", 50000, "Maximum number of rows to import excl"),
    // User login
    LOGIN_COUNT("loginCount", 5, "Maximum number of consecutive login authentication failures"),
    LOGIN_FILED_LOCK_TIME_DESC("loginFailedlockTimeDesc", "24 hours", "The time description of the account being locked after a login failure. Please match it with the cache time configuration. The cache configuration is 24h. Here it is recommended to configure 24 hours. The default is 24 hours."),
    PWD_LOSE_COUNT("pwdLose", 180, "Change the next password expiration date"),
    PWD_CHECK_RULE_KEY("checkingRuleKey", "^[A-Za-z0-9_!@#$%&*]{6,20}$", "Password Rules"),
    PWD_CHECK_RULE_TXT("checkingRuleTxt", "The password is between 6 and 20 characters long and is a combination of numbers and letters.", "Password Rules Tips"),
    IS_OPEN_RESET_PWD_BY_EMAIL("isOpenResetPwdByEmail", false, "Whether to enable the email password retrieval function"),
    LOGIN_CAPTCHA_KEY("captchaSwitch", false, "Whether the user logs in to verify the verification code"),
    LOGIN_RESET_PWD("isResetPwd", false, "Whether the initial password must be reset before logging in"),
    CHANGE_PWD_iS_LOG_OUT("changePwdIsLogOut", false, "Change password to log out"),
    CHANGE_PWD_iS_Exit_SYSTEM("changePwdIsExitSystem", false, "Change the password to force exit the system"),

    // WeChat Business
    WX_QY_APP_SECRET("wxqy_appsecret", "", ""),

    // Mailbox Configuration
    EMAIL_HOST("email_host", "", "email_host"),
    EMAIL_PORT("email_port", "", "email_port"),
    EMAIL_SSL("email_ssl", "", "email_ssl"),
    EMAIL_NICKNAME("email_nickname", "", "email_nickname"),
    EMAIL_ADDRESS("email_address", "", "email_address"),
    EMAIL_PASSWORD("email_password", "", "email_password"),


    IS_INTERFACE_AUTH("is_interface_auth", false, "Whether to enable interface level authentication"),

    /**
     * Transaction message retry times
     */
    TRXM_RETRY_TIMES("ab.trxm.retry-times", 3, "Transaction message retry times"),

    /**
     * Baidu SDK APPID
     */
    BAIDU_SDK_APPID("baidu.sdk.app-id", "", "Baidu SDK APPID"),

    /**
     * Baidu SDK Key
     */
    BAIDU_SDK_SECRET("baidu.sdk.secret", "", "Baidu SDK Key"),


    ONLINE_DOC_SERVICE_URL("online_doc_service_url","http://192.168.1.141:18080","Online Documentation Service Address"),
    AGILEBPM_SERVICE_URL("agilebpm_service_url","http://192.168.1.6:8080","agilebpm service address"),
    ONLINE_DOC_SERVICE_AGENT_PREFIX("online_doc_service_agent_prefix","/weboffice","Online Document Service Proxy Prefix"),

    PC_URL("pcUrl","http://localhost:8086","PC service address"),
    APP_URL("appUrl","http://localhost:5173/#","The address of the mobile server"),
    MOBILE_URL("mobileUrl","http://localhost:5173","The address of the mobile server"),
    WORD_OFFICE_HOME("ab.word.office-home", "", "LibreOfficePortable file location"),
    WORD_OFFICE_HOST("ab.word.office-host", "", "LibreOfficePortable online version"),
    SCRIPT_LOG("script_log", "false", "Script log switch"),

    DISABLED_AREA("DISABLED_AREA","Henan Province. Zhengzhou City, Fujian Province. Putian City,","Product prohibited areas"),

    /**
     * Is the token put into the cookie?
     */
     tokenCookieSwitch("ab.security.token-cookie-switch", false, "Is the token put into the cookie?"),

    /**
     * System temporary user name
     */
    SYS_TEMP_USER_NAME("sys.temporary.user.name","temporaryUsers","System temporary user name"),


    /**
     * The system temporarily opens the application id
     */
    SYS_TEMP_OPEN_APP_ID("sys.temporary.open.appId","openPlatform","The system temporarily opens the application id"),

    /**
     * Front-end encryption key
     */
    LE_ENCRYPTION_KEY("ab.secret-key", "am2.8basic", "Front-end encryption key"),

    /**
     * Backend interface Nginx proxy name
     */
    API_NGINX_AGENT_PREFIX("api.nginx.agent.prefix","","Backend interface Nginx proxy name"),
    
    /**
     * Whether the encoding adds scode by default
     */
    IS_SCODE("isScode", true, "Whether the encoding adds scode by default"),

    SALARY_DEF_KEY("salary_def_key", "gzffqr", "Payroll management default start process key"),

    SALARY_BO_KEY("salary_bo_key", "gzt", "Payroll management default start process key"),




    USER_REGISTER_BPM_DEF_KEY("userRegisterBpmDefKey","dstz_appstore_user_register","User registration process definition key"),
    USER_REGISTER_BO_CODE("userRegisterBoCode","dstz_appstore_user","User registration business object code"),

    DD_CALL_BACK_URL("dd_call_back_url","/api/ab-bpm/bpm/task/dealTaskByThird","The callback registration address when clicking \"Agree\" on the DingTalk card"),
    ;


    private final String key;

    private final Object defaultValue;

    private final String desc;

    PropertyEnum(String key, Object defaultValue, String desc) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.desc = desc;
    }

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

}
