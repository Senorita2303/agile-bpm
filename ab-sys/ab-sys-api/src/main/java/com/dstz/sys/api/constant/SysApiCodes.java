package com.dstz.sys.api.constant;

import com.dstz.base.common.codes.IBaseCode;

/**
 * @Name SysApiCodes
 * @description: System Settings Status Code
 */
public enum SysApiCodes implements IBaseCode {

    KEY_WORD_DUPLICATE("CodeDuplicate", "{}Already exists in the system!"),
    CODE_DUPLICATE("CodeDuplicate", "Code already exists!"),
    CODE_DUPLICATE_CODE("CodeDuplicateCode", "Code already exists! {}"),
    NAME_DUPLICATE("NameDuplicate", "Name already exists!"),


    //System properties related
    NOT_FOND_ENV_PROPERTY("NotFoundEnvProperty", "Unable to find the correct environment property configuration"),

    //Data dictionary related
    DICT_KEY_TYPE_ERROR("DictKeyTypeError", "The dictionary type can only be dict/node"),
    DICT_DELETE_ERROR("DictDeleteError", "The system built-in [{}] cannot be deleted"),

    //Holidays
    WORK_CALENDAR_INITIAL_ERROR("WorkCalendarInitialError", "The current year has been initialized"),
    WORK_CALENDAR_START_END_TIME_ERROR("WorkCalendarStartEndTimeError", "Start date is greater than end date"),
    WORK_CALENDAR_TIME_ERROR("WorkCalendarTimeError", "There are dates in this time period that have been set with different holiday types. Please delete them and add them or update them directly."),
    WORK_CALENDAR_SYSTEM_ERROR("WorkCalendarSystemError", "The system is inconsistent with the type"),
    WORK_CALENDAR_CANT_NOT_CREATE("WorkCalendarCantNotCreate", "Creation is not supported"),
    WORK_CALENDAR_CANT_NOT_UPDATE("WorkCalendarCantNotUpdate", "Update not supported"),
    WORK_CALENDAR_YEAR_NOT_INITIAL("WorkCalendarYearNotInitial", "Failed to obtain working days: Please maintain {} statutory holidays"),
    HOLIDAY_CONF_NAME_DUPLICATE("HolidayConfNameDuplicate", "Repeatedly add, there can only be one holiday with the same name on the same date"),
    HOLIDAY_CONF_NOT_FOUND("HolidayConfNotFound", "Holiday configuration does not exist"),
    WORKCALENDAR_NOT_INIT("sys-workCalendar-not-init","Working day calculation is not available, please configure {} year holidays!"),

    //Schedule related
    SCHEDULE_CONF_TIME_ERROR("ScheduleConfTimeError", "The completion time cannot be less than the actual start date"),

    //Work handover related
    WORK_HANDOVER_USER_ERROR("WorkHandoverUserError", "User ({})'s work has been handed over, please do not assign it as a receiver!"),

    //Data source related
    DATA_SOURCE_CLASSPATH_ERROR("DataSourceClasspathError", "classPath[{}] is not a subclass of javax.sql.DataSource"),
    DATA_SOURCE_CLASSPATH_PARAM_ERROR("DataSourceClasspathParamError", "Exception in getting parameters based on classPath[{}]"),
    DATA_SOURCE_NOT_FOUND_ERROR("DataSourceNotFoundError", "The data source ({}) is unavailable, please contact the administrator"),
    DATA_SOURCE_ATTRIBUTE_IS_EMPTY("DataSourceAttributeIsempty","The data source ({}) property list is empty"),
    DATASOURCE_CONNECTION_EXCEPTION("DatasourceConnectionException", "Data source connection exception:{}"),

    //File operation related
    FILE_NOT_FOUND_ERROR("FileNotFoundError", "Attachment [{}] does not exist"),
    FILE_NAME_LENGTH_ERROR("FileNameLengthError", "The file name cannot be longer than 60 characters"),
    FILE_CREATE_DIR_ERROR("CanNotCreateDirectory","Failed to create directory{}"),
    FILE_OPEN_FILE_ERROR("OpenFileError","Failed to open the online document, [{}]"),
    FILE_CREATE_FILE_ERROR("CreateFileError","Failed to create document!"),
    FILE_TEMPLATE_ERROR("FileTemplateError","File template error! [{}]"),

    //Audit log related
    OPERATE_LOG_REMOVE_ERROR("OperateLogRemoveError", "There are [{}] operation logs associated, please delete the operation logs before operating"),

    //Serial number related
    SERIAL_NO_CODE_NOT_FOUND_ERROR("SerialNoCodeNotFound", "Can't find any information about the serial number!"),
    SERIAL_NO_GET_PARAM_ERROR("SerialNoGetParamError", "Error in getting serial number parameters, {} parameters are required"),
    SERIAL_NO_REVIVE_SAVE_ERROR("SerialNoReviveLogSaveError", "Failed to save the serial number rule record, please contact the administrator"),
    SERIAL_NO_BUILD_ERROR("SerialNoBuildError", "The service for generating serial number is busy, please try again later!"),
    SERIAL_NO_EXECUTE_SCRIPT_ERROR("SerialNoeExecuteScriptError", "The serial number parameter script execution error, please check! {}"),

    //CMS related
    NOTIFY_HAS_USED("NotifyHasUsed", "The announcement list is using this type, please delete the associated announcement list first!"),
    REFLEX_WARNING("reflexWarning", "The comment module background reflection is abnormal!"),
    JSON_CONVERSION_ERROR("jsonConversionError", "In the translation list, the data named [{}] is abnormal, please modify this data again"),
    TEMPLATE_CONVERSION_ERROR("templateConversionError", "Template data conversion error, please check the message template content!"),
    INIT_DATA_CANT_DELETE("initDataCantDelete", "Common phrases [{}] are built-in data and cannot be deleted!"),
    KEY_REPEAT("keyRepeat", "Alias ​​[{}] conflicts with existing alias [{}]"),

    CONNECT_RECORD_ERROR("ConnectRecordError", "Information:{}"),

    SYS_CONFIG_NOT_FOUND("sys_config_not_found", "System configuration not found, code:{}"),


    /**
     * Temporary link related
     */
    TEMPORARY_LINK_ENABLED("TemporaryLinkEnabled","Temporary link is not available! {}"),

    TEMPORARY_LINK_NOT_FOUND("TemporaryLinkNotFound","The temporary link does not exist!"),


    SYSCONFIG_CODE_ISUPDATA("sysconfig_code_isupdata", "The system configuration code cannot be modified: {}"),
    ;
    /**
     * Data source connection abnormality
     */


    private final String code;

    private final String message;

    SysApiCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
