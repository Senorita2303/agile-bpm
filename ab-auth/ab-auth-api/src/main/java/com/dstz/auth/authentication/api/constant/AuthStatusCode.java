package com.dstz.auth.authentication.api.constant;

import com.dstz.base.common.codes.IBaseCode;

/**
 * Authentication interface response code
 *
 */
public enum AuthStatusCode implements IBaseCode {

    /**
     * The required parameter is empty
     */
    PARAM_CLIENT_ID_IS_NOT_FOUND("param_client_id_is_not_found", "The required parameter clientId is empty"),
    /**
     * The account or password is incorrect
     */
    LOGIN_ERROR("login_error", "The account or password is incorrect"),

    USER_NAME_OR_PASSORD_ERROR("user_name_or_passord_error", "{}"),
    /**
     * Application configuration error
     */
    APP_CONFIG_ERROR("app_config_error", "Application configuration error"),
    /**
     * Account not found
     */
    ACCOUNT_NOT_FIND("account_not_find", "Account does not exist"),
    /**
     * Account cannot be empty
     */
    ACCOUNT_CANNOT_BE_EMPTY("account_cannot_be_empty", "Account cannot be empty"),
    /**
     * Password cannot be empty
     */
    PASSWORD_CANNOT_BE_EMPTY("password_cannot_be_empty", "Password cannot be empty"),
    /**
     * Account disabled
     */
    ACCOUNT_DISABLED("account_disabled", "Account disabled"),
    /**
     * Account locked
     */
    ACCOUNT_IS_LOCKED("account_is_locked", "Account locked"),
    /**
     * Account Expired
     */
    ACCOUNT_HAS_EXPIRED("account_has_expired", "Account Expired"),
    /**
     * If the account or password is incorrect too many times, login will be prohibited
     */
    DISABLE_LOGIN("disable_login", "If the account or password is incorrect too many times, login will be prohibited{}"),

    DISABLE_LOGIN_WARN("disable_login_warn","The account password is incorrect and has failed {} times. If you fail {} times in a row, you will be banned from logging in{}"),
    /**
     * Verification code cannot be empty
     */
    CAPTCHA_CANNOT_BE_EMPTY("captcha_cannot_be_empty", "Verification code cannot be empty"),
    /**
     * Validation Type
     */
    GRANTTYPE_CANNOT_BE_EMPTY("granttype_cannot_be_empty", "Validation type cannot be empty"),
    /**
     * Verification code error
     */
    CAPTCHA_ERROR("captcha_error", "Verification code error, please try again"),
    /**
     * The password is the initial password
     */
    PASSWORD_NEEDS_TO_BE_CHANGED("password_needs_to_be_changed", "The password is the initial password. Please change the password before logging in."),
    /**
     * The current user has not yet assigned any resources
     */
    USER_HAS_NOT_ASSIGNED_ANY_RESOURCES("user_has_not_assigned_any_resources", "The current user has not yet assigned any resources"),

    APPLICATION_NO_PERMISSIONS("application_no_permissions", "The app does not have permission:{}"),

    RESOURCES_CODE_REPEAT("resources_code_repeat", "Alias ​​already exists, please modify! :{}"),

    DELETE_RESOURCES_ERROR("delete_resources_error", "Failed to delete subsystem resources"),

    SYS_IS_NOT_DEFINITION("sys_is_not_definition", "Current System“{} ”does not exist! Please add a subsystem and configure the resource menu!\" : \"You have no current system“{}”Please contact the administrator."),

    PARAM_IS_NULL("param_is_null", "Required parameter {} is empty"),

    APPLICATION_GET_TOKEN_ERROR("application_get_token_error", "User: {} Get token exception clientid: {}"),
    APPLICATION_REFRESH_TOKEN_ERROR("application_refresh_token_error", "Refresh token exception clientid: {}"),
    /**
     * No data found based on the current clientid
     */
    LOADCLIENT_BY_CLIENTID_ERROR("loadclient_by_clientid_error", "No data found based on the current clientid"),

    /**
     * There is no client authentication. Try adding an appropriate authentication filter
     */
    NO_CLIENT_AUTHENTICATION("no_client_authentication", "No matching authentication filters found"),

    LOGIN_TIMEOUT("LOGIN_TIMEOUT", "Login timeout, please log in again"),

    NO_AUTH_TOKEN("no_auth_token", "The request did not pass in authentication information"),

    TOKEN_INVALID("token_invalid", "Login timeout"),

    NOT_PERMISSION("not_permission", "Insufficient permissions"),

    LOAD_CLIENT_BY_CLIENTID_ERROR("load_client_by_clientid_error", "Loading application information exception:{}"),

    NO_FIND_APP("no_find_app", "No corresponding application found for client_id: {}"),

    RESOUCE_TYPE_CONSTANT_ERROR("resouce_type_constant_error", "For resources of menu type, the parent resource {} must also be a menu!"),

    METHOD_NOT_ALLOWED("method_not_allowed", "Method not allowed"),

    SERVICE_ERROR("service_error", "Internal Error"),

    LOGIN_AUTHORIZATION_FILTER_ERROR("login_authorization_filter_error", "Authentication filter AuthorizationTokenCheckFilter gets token exception"),

    /**
     * Illegal current user organization, usually causes this error, for example, the administrator assigns an organization or the user modifies the request parameters
     */
    ILLEGAL_CURRENT_ORG("IllegalCurrentOrg", "Illegal current user organization, illegal organization code:{}"),

    /**
     * User has no organization assigned
     */
    USER_UNABSORBED_ORG("UNABSORBED_ORG", "User has no organization assigned"),

    /**
     * Applying menuless resources
     */
    APP_NO_RESOURCE("AppNoResource", "Failed to switch system. There is no accessible menu resource under this system."),


    /**
     * The request is not assigned to any role, or the interface does not exist
     */
    API_NONEXISTENT_OR_NO_ACCESS("api_nonexistent_or_no_access", "The request is not assigned to any role, or the interface does not exist:{}"),


    /**
     * You do not have permission to access this interface
     */
    API_NO_ACCESS("api_no_access", "You do not have permission to access this interface:"),


    APPCLICATION_ENABLE_DEFAULT_MOBILE_APP_NOT_FIND("appclication_enable_default_mobile_app_not_find", "The system cannot find the default mobile application. Please configure the default mobile application first."),


    ;

    private final String code;
    private final String desc;

    AuthStatusCode(String code, String description) {
        this.code = code;
        this.desc = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return desc;
    }
}
