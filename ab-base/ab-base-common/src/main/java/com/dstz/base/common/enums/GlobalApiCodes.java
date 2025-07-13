package com.dstz.base.common.enums;

import com.dstz.base.common.codes.IBaseCode;


/**
 * Global interface response code
 *
 */
public enum GlobalApiCodes implements IBaseCode {

    /**
     * Operation successful
     */
    SUCCESS("Success", "Operation successful"),

    /**
     * Illegal parameters.
     */
    PARAMETER_INVALID("ParameterInvalid", "Illegal parameters {}"),
    
	/**
     * Parameter unallowed.
     */
    PARAMETER_UNALLOWED("ParameterUnallowed", "Parameter not allowed {}"),

    /**
     * User not authorized to operate on the specified resource.
     */
    ACCESS_FORBIDDEN("AccessForbidden", "User not authorized to operate on the specified resource."),

    /**
     * The specified resource is not found.
     */
    RESOURCE_NOT_FOUND("ResourceNotFound", "The specified resource is not found."),
    
    /**
     * Parsing failed
     */
    PARSE_ERROR ("parseError", "{} parsing failed!"),
    
    /**
     * {}Data already exists{}
     */
    DATA_DUPLICATION ("DataDuplication", "{}Data already exists{}"),
    
    DATA_NOT_FOUND ("DataNotFound", "{}Data does not exist{}"),

    
    NO_LOGIN_USER ("noLoginUser", "The logged in user does not exist!"),
    
    /**
     * Deletion failed, cascade data exists
     */
    DELETE_FAILED_HAS_ASSOCIATED_DATA ("deleteFailedHasAssociatedData", "Deletion failed, there is associated data: {}"),
    
    /**
     * General Exceptions
     */
    BASE_COMMON_ERROR ("baseCommonError", "base module common exception"),

    /**
     * The request processing has failed due to some unknown error.
     */
    INTERNAL_ERROR("InternalError", "Internal system error"),

    /**
     * Remote call error
     */
    REMOTE_CALL_ERROR("RemoteCallError", "{}"),

    /**
     * Login session timed out
     */
    LOGIN_INVALID("LoginValid", "Login session timed out, please log in again!"),

    /**
     * Request rate limiting
     */
    REQUEST_FLOW_LIMITING("RequestFlowLimiting", "The server is busy, please try again later!"),

    /**
     * Service degradation
     */
    SERVICE_DEGRADE("ServiceDegrade", "The service is unavailable, please try again later!"),

    /**
     * The data has been updated by others (optimistic lock)
     */
    DATA_VERSION_OLD("DataVersionOld", "The data has been updated, please refresh the page and edit and save again"),

    FILE_PATH_NOT_FOUND_ERROR("FileNotFoundError", "The attachment does not exist! , The attachment address is [{}]"),
    
    /**
     * The script execution log currently relies on the exception log output and viewing, so an exception flag is declared
     */
    SCRIPT_LOG("ScriptLog", "{}"),

    /**
     * XSS Field Check
     */
    XSS_INJECT("XssInject", "There is a risk of Xss injection, please modify the field value: {}");

    private final String code;

    private final String message;

    GlobalApiCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
